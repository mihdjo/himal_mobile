package com.himal.mobile.ui.plan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.PlanRepository
import com.himal.mobile.data.repository.PlanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlanViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        PlanRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(PlanUiState())

    val uiState: StateFlow<PlanUiState> =
        _uiState.asStateFlow()

    fun loadPlan() {

        _uiState.value =
            PlanUiState(
                isLoading = true
            )

        viewModelScope.launch {

            when (
                val result = repository.getPlan()
            ) {

                is PlanResult.Success -> {

                    _uiState.value =
                        PlanUiState(
                            items = result.items
                        )
                }

                is PlanResult.Error -> {

                    _uiState.value =
                        PlanUiState(
                            errorMessage = result.message
                        )
                }

                PlanResult.Unauthorized -> {

                    _uiState.value =
                        PlanUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun reset() {
        _uiState.value = PlanUiState()
    }
}