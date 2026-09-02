package com.himal.mobile.ui.packing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.PackingListRepository
import com.himal.mobile.data.repository.PackingListResult
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

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            aggregated =
                                result.aggregated,
                            grouped =
                                result.grouped
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