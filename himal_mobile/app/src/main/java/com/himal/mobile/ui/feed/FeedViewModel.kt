package com.himal.mobile.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.EquipmentManagementResult
import com.himal.mobile.data.repository.FeedRepository
import com.himal.mobile.data.repository.FeedResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        FeedRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(FeedUiState())

    val uiState: StateFlow<FeedUiState> =
        _uiState.asStateFlow()

    fun loadFeed() {

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                sessionExpired = false
            )

        if (_uiState.value.expeditionTypes.isEmpty()) {
            loadTypes()
        }

        viewModelScope.launch {

            when (val result = repository.getFeed()) {

                is FeedResult.Success -> {

                    _uiState.value =
                        FeedUiState(
                            isLoading = false,
                            expeditions = result.expeditions
                        )
                }

                is FeedResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                }

                FeedResult.Unauthorized -> {

                    _uiState.value =
                        FeedUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun onSearchChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                search = value,
                errorMessage = null
            )
    }

    fun onLocationChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                location = value,
                errorMessage = null
            )
    }

    fun onDifficultySelected(
        value: String?
    ) {

        _uiState.value =
            _uiState.value.copy(
                difficulty = value,
                errorMessage = null
            )
    }

    fun onTypeSelected(
        id: Long?
    ) {

        _uiState.value =
            _uiState.value.copy(
                selectedTypeId = id,
                errorMessage = null
            )
    }

    fun onMaxDurationChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                maxDuration = value,
                errorMessage = null
            )
    }

    fun onMaxDistanceChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                maxDistance = value,
                errorMessage = null
            )
    }

    fun toggleFilters() {

        _uiState.value =
            _uiState.value.copy(
                showFilters =
                    !_uiState.value.showFilters
            )
    }

    private fun loadTypes() {

        viewModelScope.launch {

            when (
                val result =
                    repository.getExpeditionTypes()
            ) {

                is EquipmentManagementResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            expeditionTypes =
                                result.data
                        )
                }

                is EquipmentManagementResult.Error -> {
                }

                EquipmentManagementResult.Unauthorized -> {

                    _uiState.value =
                        _uiState.value.copy(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun applyFilters() {

        val state =
            _uiState.value

        val maxDuration =
            if (
                state.maxDuration.isBlank()
            ) {

                null

            } else {

                state.maxDuration
                    .toIntOrNull()
                    ?: run {

                        _uiState.value =
                            state.copy(
                                errorMessage =
                                    "Maksimalno trajanje mora biti ceo broj."
                            )

                        return
                    }
            }

        if (
            maxDuration != null &&
            maxDuration < 1
        ) {

            _uiState.value =
                state.copy(
                    errorMessage =
                        "Maksimalno trajanje mora biti veće od 0."
                )

            return
        }

        val maxDistance =
            if (
                state.maxDistance.isBlank()
            ) {

                null

            } else {

                state.maxDistance
                    .replace(",", ".")
                    .toDoubleOrNull()
                    ?: run {

                        _uiState.value =
                            state.copy(
                                errorMessage =
                                    "Maksimalna dužina mora biti broj."
                            )

                        return
                    }
            }

        if (
            maxDistance != null &&
            maxDistance <= 0
        ) {

            _uiState.value =
                state.copy(
                    errorMessage =
                        "Maksimalna dužina mora biti veća od 0."
                )

            return
        }

        _uiState.value =
            state.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getFeed(
                        search =
                            state.search
                                .trim()
                                .ifBlank {
                                    null
                                },

                        location =
                            state.location
                                .trim()
                                .ifBlank {
                                    null
                                },

                        difficulty =
                            state.difficulty,

                        typeId =
                            state.selectedTypeId,

                        maxDuration =
                            maxDuration,

                        maxDistance =
                            maxDistance
                    )
            ) {

                is FeedResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            expeditions =
                                result.expeditions,
                            errorMessage = null
                        )
                }

                is FeedResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                result.message
                        )
                }

                FeedResult.Unauthorized -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun clearFilters() {

        _uiState.value =
            _uiState.value.copy(
                search = "",
                location = "",
                difficulty = null,
                selectedTypeId = null,
                maxDuration = "",
                maxDistance = "",
                errorMessage = null
            )

        loadFeed()
    }

    fun reset() {
        _uiState.value = FeedUiState()
    }
}