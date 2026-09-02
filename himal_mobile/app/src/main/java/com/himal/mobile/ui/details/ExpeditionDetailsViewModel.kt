package com.himal.mobile.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.local.EquipmentChecklistManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.ExpeditionDetailsRepository
import com.himal.mobile.data.repository.ExpeditionDetailsResult
import com.himal.mobile.data.repository.EquipmentResult
import com.himal.mobile.data.repository.ExpeditionMembershipResult
import com.himal.mobile.data.repository.ActionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpeditionDetailsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        ExpeditionDetailsRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(
            ExpeditionDetailsUiState()
        )

    private val checklistManager =
        EquipmentChecklistManager(
            application.applicationContext
        )

    val uiState: StateFlow<ExpeditionDetailsUiState> =
        _uiState.asStateFlow()

    fun loadExpedition(id: Long) {

        _uiState.value =
            ExpeditionDetailsUiState(
                isLoading = true
            )

        viewModelScope.launch {

            when (
                val expeditionResult =
                    repository.getExpedition(id)
            ) {

                is ExpeditionDetailsResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            expedition =
                                expeditionResult.expedition
                        )

                    when (
                        val equipmentResult =
                            repository.getEquipment(id)
                    ) {

                        is EquipmentResult.Success -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    equipment =
                                        equipmentResult.equipment,
                                    equipmentErrorMessage = null
                                )
                        }

                        is EquipmentResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    equipment = emptyList(),
                                    equipmentErrorMessage =
                                        equipmentResult.message
                                )
                        }

                        EquipmentResult.Unauthorized -> {

                            _uiState.value =
                                ExpeditionDetailsUiState(
                                    sessionExpired = true
                                )
                        }
                    }

                    when (
                        val membershipResult =
                            repository.getMembershipStatus(id)
                    ) {

                        is ExpeditionMembershipResult.Success -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    isSaved =
                                        membershipResult.isSaved,
                                    isInPlan =
                                        membershipResult.isInPlan
                                )
                        }

                        is ExpeditionMembershipResult.Error -> {

                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    actionErrorMessage =
                                        membershipResult.message
                                )
                        }

                        ExpeditionMembershipResult.Unauthorized -> {

                            _uiState.value =
                                ExpeditionDetailsUiState(
                                    sessionExpired = true
                                )
                        }
                    }
                }


                is ExpeditionDetailsResult.Error -> {

                    _uiState.value =
                        ExpeditionDetailsUiState(
                            errorMessage =
                                expeditionResult.message
                        )
                }

                ExpeditionDetailsResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionDetailsUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun toggleSaved() {

        val expeditionId =
            _uiState.value
                .expedition
                ?.idEkspedicije
                ?: return

        if (_uiState.value.isSavedLoading) {
            return
        }

        val currentlySaved =
            _uiState.value.isSaved

        _uiState.value =
            _uiState.value.copy(
                isSavedLoading = true,
                actionErrorMessage = null
            )

        viewModelScope.launch {

            val result =
                if (currentlySaved) {

                    repository.removeSavedExpedition(
                        expeditionId
                    )

                } else {

                    repository.saveExpedition(
                        expeditionId
                    )
                }

            when (result) {

                ActionResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSavedLoading = false,
                            isSaved = !currentlySaved
                        )
                }

                is ActionResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSavedLoading = false,
                            actionErrorMessage =
                                result.message
                        )
                }

                ActionResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionDetailsUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun togglePlan() {

        val expeditionId =
            _uiState.value
                .expedition
                ?.idEkspedicije
                ?: return

        if (_uiState.value.isPlanLoading) {
            return
        }

        val currentlyInPlan =
            _uiState.value.isInPlan

        _uiState.value =
            _uiState.value.copy(
                isPlanLoading = true,
                actionErrorMessage = null
            )

        viewModelScope.launch {

            val result =
                if (currentlyInPlan) {

                    repository.removeFromPlan(
                        expeditionId
                    )

                } else {

                    repository.addToPlan(
                        expeditionId
                    )
                }

            when (result) {

                ActionResult.Success -> {

                    if (currentlyInPlan) {

                        checklistManager.clearExpedition(
                            expeditionId
                        )
                    }

                    _uiState.value =
                        _uiState.value.copy(
                            isPlanLoading = false,
                            isInPlan = !currentlyInPlan
                        )
                }

                is ActionResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isPlanLoading = false,
                            actionErrorMessage =
                                result.message
                        )
                }

                ActionResult.Unauthorized -> {

                    _uiState.value =
                        ExpeditionDetailsUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun reset() {
        _uiState.value =
            ExpeditionDetailsUiState()
    }
}