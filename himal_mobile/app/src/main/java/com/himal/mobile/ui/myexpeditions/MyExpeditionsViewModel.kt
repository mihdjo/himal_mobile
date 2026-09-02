package com.himal.mobile.ui.myexpeditions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.ActionResult
import com.himal.mobile.data.repository.MyExpeditionsRepository
import com.himal.mobile.data.repository.MyExpeditionsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyExpeditionsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        MyExpeditionsRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(
            MyExpeditionsUiState()
        )

    val uiState: StateFlow<MyExpeditionsUiState> =
        _uiState.asStateFlow()

    fun loadExpeditions() {

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getMyExpeditions()
            ) {

                is MyExpeditionsResult.Success -> {

                    _uiState.value =
                        MyExpeditionsUiState(
                            expeditions =
                                result.expeditions
                        )
                }

                is MyExpeditionsResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                result.message
                        )
                }

                MyExpeditionsResult.Unauthorized -> {

                    _uiState.value =
                        MyExpeditionsUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun deleteExpedition(
        id: Long
    ) {

        if (_uiState.value.deletingId != null) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                deletingId = id,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.delete(id)
            ) {

                ActionResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            deletingId = null,
                            expeditions =
                                _uiState.value
                                    .expeditions
                                    .filterNot {
                                        it.idEkspedicije == id
                                    }
                        )
                }

                is ActionResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            deletingId = null,
                            errorMessage =
                                result.message
                        )
                }

                ActionResult.Unauthorized -> {

                    _uiState.value =
                        MyExpeditionsUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun reset() {

        _uiState.value =
            MyExpeditionsUiState()
    }
}