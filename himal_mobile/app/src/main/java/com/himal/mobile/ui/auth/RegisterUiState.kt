package com.himal.mobile.ui.auth

data class RegisterUiState(
    val ime: String = "",
    val prezime: String = "",
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val datumRodjenja: String = "",

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val registrationSuccessful: Boolean = false,
    val registeredUsername: String? = null
)