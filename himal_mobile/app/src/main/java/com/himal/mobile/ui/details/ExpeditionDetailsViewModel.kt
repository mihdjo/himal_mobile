package com.himal.mobile.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.ExpeditionDetailsRepository
import com.himal.mobile.data.repository.ExpeditionDetailsResult
import com.himal.mobile.data.repository.EquipmentResult
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
                                    isLoading = false,
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

    fun reset() {
        _uiState.value =
            ExpeditionDetailsUiState()
    }
}