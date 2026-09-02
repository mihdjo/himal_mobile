package com.himal.mobile.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.repository.AuthRepository
import com.himal.mobile.data.repository.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.himal.mobile.data.local.EquipmentChecklistManager

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        api = RetrofitClient.api,
        sessionManager = SessionManager(
            application.applicationContext
        )
    )

    private val _uiState =
        MutableStateFlow(AuthUiState())

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    private val checklistManager =
        EquipmentChecklistManager(
            application.applicationContext
        )

    init {
        restoreSession()
    }
    fun onUsernameChange(username: String) {

        _uiState.value =
            _uiState.value.copy(
                username = username,
                errorMessage = null
            )
    }

    fun onPasswordChange(password: String) {

        _uiState.value =
            _uiState.value.copy(
                password = password,
                errorMessage = null
            )
    }

    fun login() {

        val username =
            _uiState.value.username.trim()

        val password =
            _uiState.value.password

        if (username.isBlank()
            || password.isBlank()
        ) {

            _uiState.value =
                _uiState.value.copy(
                    errorMessage =
                        "Unesite korisničko ime i lozinku."
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.login(
                        username,
                        password
                    )
            ) {

                is LoginResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isCheckingSession = false,
                            isLoggedIn = true,
                            loggedInUsername =
                                result.data.username,
                            password = "",
                            errorMessage = null
                        )
                }

                is LoginResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            errorMessage =
                                result.message
                        )
                }
            }
        }
    }

    fun logout() {

        viewModelScope.launch {

            repository.logout()

            checklistManager.clear()

            _uiState.value =
                AuthUiState(
                    isCheckingSession = false
                )
        }
    }

    private fun restoreSession() {

        viewModelScope.launch {

            val token = repository.getSavedToken()

            _uiState.value =
                _uiState.value.copy(
                    isCheckingSession = false,
                    isLoggedIn = !token.isNullOrBlank()
                )
        }
    }
}