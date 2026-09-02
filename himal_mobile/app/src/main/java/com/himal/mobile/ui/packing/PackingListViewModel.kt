package com.himal.mobile.ui.packing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.PackingListRepository
import com.himal.mobile.data.repository.PackingListResult
import com.himal.mobile.data.local.EquipmentChecklistManager
import com.himal.mobile.data.local.checklistKey
import com.himal.mobile.data.repository.ActionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PackingListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        PackingListRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(
            PackingListUiState()
        )

    private val checklistManager =
        EquipmentChecklistManager(
            application.applicationContext
        )

    val uiState: StateFlow<PackingListUiState> =
        _uiState.asStateFlow()

    fun loadPackingList() {

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                sessionExpired = false
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getPackingList()
            ) {

                is PackingListResult.Success -> {

                    val savedPreparedItems =
                        checklistManager.getPreparedItems()

                    val validKeys =
                        result.grouped
                            .flatMap { group ->

                                group.oprema.map { equipment ->

                                    checklistKey(
                                        group.idEkspedicije,
                                        equipment.idOpreme
                                    )
                                }
                            }
                            .toSet()

                    // Ako je ekspedicija uklonjena iz plana
                    // ili je njena oprema promenjena,
                    // brišemo zastarele lokalne checkbox-e.
                    val cleanedPreparedItems =
                        savedPreparedItems.intersect(
                            validKeys
                        )

                    checklistManager.replacePreparedItems(
                        cleanedPreparedItems
                    )

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            aggregated = result.aggregated,
                            grouped = result.grouped,
                            preparedItems =
                                cleanedPreparedItems,
                            errorMessage = null
                        )

                    syncStatuses(
                        expeditionIds =
                            result.grouped
                                .map { it.idEkspedicije }
                                .toSet(),
                        preparedItems =
                            cleanedPreparedItems
                    )
                }

                is PackingListResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                result.message
                        )
                }

                PackingListResult.Unauthorized -> {

                    _uiState.value =
                        PackingListUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun toggleEquipment(
        expeditionId: Long,
        equipmentId: Long,
        checked: Boolean
    ) {

        viewModelScope.launch {

            val key =
                checklistKey(
                    expeditionId,
                    equipmentId
                )

            val updated =
                _uiState.value
                    .preparedItems
                    .toMutableSet()

            if (checked) {
                updated.add(key)
            } else {
                updated.remove(key)
            }

            val updatedSet =
                updated.toSet()

            // Optimistic UI update
            _uiState.value =
                _uiState.value.copy(
                    preparedItems = updatedSet,
                    checklistErrorMessage = null
                )

            checklistManager
                .replacePreparedItems(
                    updatedSet
                )

            syncStatuses(
                expeditionIds =
                    setOf(expeditionId),
                preparedItems =
                    updatedSet
            )
        }
    }

    fun toggleAggregatedEquipment(
        equipmentId: Long,
        checked: Boolean
    ) {

        viewModelScope.launch {

            val affectedGroups =
                _uiState.value.grouped
                    .filter { group ->

                        group.oprema.any {
                            it.idOpreme ==
                                    equipmentId
                        }
                    }

            val updated =
                _uiState.value
                    .preparedItems
                    .toMutableSet()

            affectedGroups.forEach { group ->

                val key =
                    checklistKey(
                        group.idEkspedicije,
                        equipmentId
                    )

                if (checked) {
                    updated.add(key)
                } else {
                    updated.remove(key)
                }
            }

            val updatedSet =
                updated.toSet()

            _uiState.value =
                _uiState.value.copy(
                    preparedItems =
                        updatedSet,
                    checklistErrorMessage = null
                )

            checklistManager
                .replacePreparedItems(
                    updatedSet
                )

            syncStatuses(
                expeditionIds =
                    affectedGroups
                        .map {
                            it.idEkspedicije
                        }
                        .toSet(),
                preparedItems =
                    updatedSet
            )
        }
    }

    private suspend fun syncStatuses(
        expeditionIds: Set<Long>,
        preparedItems: Set<String>
    ) {

        for (expeditionId in expeditionIds) {

            val group =
                _uiState.value.grouped
                    .firstOrNull {
                        it.idEkspedicije ==
                                expeditionId
                    }
                    ?: continue

            val ready =
                group.oprema.isNotEmpty() &&
                        group.oprema.all { equipment ->

                            checklistKey(
                                expeditionId,
                                equipment.idOpreme
                            ) in preparedItems
                        }

            // Ako backend već ima ispravno stanje,
            // nema potrebe da šaljemo PUT.
            if (group.status == ready) {
                continue
            }

            when (
                val result =
                    repository.updatePlanStatus(
                        expeditionId =
                            expeditionId,
                        status = ready
                    )
            ) {

                ActionResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            grouped =
                                _uiState.value.grouped
                                    .map {

                                        if (
                                            it.idEkspedicije ==
                                            expeditionId
                                        ) {

                                            it.copy(
                                                status = ready
                                            )

                                        } else {

                                            it
                                        }
                                    }
                        )
                }

                is ActionResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            checklistErrorMessage =
                                result.message
                        )
                }

                ActionResult.Unauthorized -> {

                    _uiState.value =
                        PackingListUiState(
                            sessionExpired = true
                        )

                    return
                }
            }
        }
    }

    fun showAll() {

        _uiState.value =
            _uiState.value.copy(
                viewMode =
                    PackingViewMode.ALL
            )
    }

    fun showGrouped() {

        _uiState.value =
            _uiState.value.copy(
                viewMode =
                    PackingViewMode.GROUPED
            )
    }

    fun reset() {
        _uiState.value =
            PackingListUiState()
    }
}