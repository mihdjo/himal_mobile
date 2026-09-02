package com.himal.mobile.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.remote.dto.RegisterRequest
import com.himal.mobile.data.repository.AuthRepository
import com.himal.mobile.data.repository.RegisterResult
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AuthRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> =
        _uiState.asStateFlow()

    fun onImeChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                ime = value,
                errorMessage = null
            )
    }

    fun onPrezimeChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                prezime = value,
                errorMessage = null
            )
    }

    fun onEmailChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                email = value,
                errorMessage = null
            )
    }

    fun onUsernameChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                username = value,
                errorMessage = null
            )
    }

    fun onPasswordChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                password = value,
                errorMessage = null
            )
    }

    fun onDatumRodjenjaChange(value: String) {
        _uiState.value =
            _uiState.value.copy(
                datumRodjenja = value,
                errorMessage = null
            )
    }

    fun register() {

        if (_uiState.value.isLoading) {
            return
        }

        val ime =
            _uiState.value.ime.trim()

        val prezime =
            _uiState.value.prezime.trim()

        val email =
            _uiState.value.email.trim()

        val username =
            _uiState.value.username.trim()

        val password =
            _uiState.value.password

        val datumRodjenja =
            _uiState.value.datumRodjenja.trim()

        val validationError =
            validate(
                ime = ime,
                prezime = prezime,
                email = email,
                username = username,
                password = password,
                datumRodjenja = datumRodjenja
            )

        if (validationError != null) {

            _uiState.value =
                _uiState.value.copy(
                    errorMessage = validationError
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            val request =
                RegisterRequest(
                    ime = ime,
                    prezime = prezime,
                    email = email,
                    username = username,
                    password = password,
                    datumRodjenja =
                        datumRodjenja
                )

            when (
                val result =
                    repository.register(request)
            ) {

                is RegisterResult.Success -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            registrationSuccessful = true,
                            registeredUsername =
                                result.user.username
                        )
                }

                is RegisterResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                result.message
                        )
                }
            }
        }
    }

    private fun validate(
        ime: String,
        prezime: String,
        email: String,
        username: String,
        password: String,
        datumRodjenja: String
    ): String? {

        if (
            ime.isBlank() ||
            prezime.isBlank() ||
            email.isBlank() ||
            username.isBlank() ||
            password.isBlank() ||
            datumRodjenja.isBlank()
        ) {
            return "Sva polja su obavezna."
        }

        if (ime.length > 50) {
            return "Ime može imati najviše 50 karaktera."
        }

        if (prezime.length > 50) {
            return "Prezime može imati najviše 50 karaktera."
        }

        if (
            email.length > 150 ||
            !email.contains("@") ||
            !email.contains(".")
        ) {
            return "Unesi ispravnu email adresu."
        }

        if (username.length !in 3..50) {
            return "Username mora imati između 3 i 50 karaktera."
        }

        if (password.length !in 6..100) {
            return "Lozinka mora imati između 6 i 100 karaktera."
        }

        val date =
            try {

                LocalDate.parse(
                    datumRodjenja
                )

            } catch (_: Exception) {

                return "Datum mora biti u formatu YYYY-MM-DD."
            }

        if (!date.isBefore(LocalDate.now())) {
            return "Datum rođenja mora biti u prošlosti."
        }

        return null
    }

    fun reset() {
        _uiState.value =
            RegisterUiState()
    }
}