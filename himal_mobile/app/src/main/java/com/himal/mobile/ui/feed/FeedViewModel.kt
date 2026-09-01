package com.himal.mobile.ui.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
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

    fun reset() {
        _uiState.value = FeedUiState()
    }
}