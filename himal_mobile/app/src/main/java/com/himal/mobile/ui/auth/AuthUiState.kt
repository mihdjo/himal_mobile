package com.himal.mobile.ui.auth

data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isCheckingSession: Boolean = true,
    val isLoggedIn: Boolean = false,
    val loggedInUsername: String? = null,
    val errorMessage: String? = null
)