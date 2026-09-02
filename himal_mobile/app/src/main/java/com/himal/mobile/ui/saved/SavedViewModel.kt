package com.himal.mobile.ui.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.SavedRepository
import com.himal.mobile.data.repository.SavedResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        SavedRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(SavedUiState())

    val uiState: StateFlow<SavedUiState> =
        _uiState.asStateFlow()

    fun loadSaved() {

        _uiState.value =
            SavedUiState(
                isLoading = true
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getSavedExpeditions()
            ) {

                is SavedResult.Success -> {

                    _uiState.value =
                        SavedUiState(
                            expeditions =
                                result.expeditions
                        )
                }

                is SavedResult.Error -> {

                    _uiState.value =
                        SavedUiState(
                            errorMessage =
                                result.message
                        )
                }

                SavedResult.Unauthorized -> {

                    _uiState.value =
                        SavedUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun reset() {
        _uiState.value = SavedUiState()
    }
}