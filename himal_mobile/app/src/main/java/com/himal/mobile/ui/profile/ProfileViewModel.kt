package com.himal.mobile.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.remote.dto.UpdateKorisnikRequest
import com.himal.mobile.data.repository.ProfileRepository
import com.himal.mobile.data.repository.ProfileResult
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        ProfileRepository(
            api = RetrofitClient.api,
            sessionManager = SessionManager(
                application.applicationContext
            )
        )

    private val _uiState =
        MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    fun loadProfile() {

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        viewModelScope.launch {

            when (
                val result =
                    repository.getProfile()
            ) {

                is ProfileResult.Success -> {

                    _uiState.value =
                        ProfileUiState(
                            user = result.user,
                            ime = result.user.ime,
                            prezime = result.user.prezime,
                            email = result.user.email,
                            datumRodjenja =
                                result.user.datumRodjenja
                        )
                }

                is ProfileResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            errorMessage =
                                result.message
                        )
                }

                ProfileResult.Unauthorized -> {

                    _uiState.value =
                        ProfileUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    fun beginEdit() {

        val user =
            _uiState.value.user
                ?: return

        _uiState.value =
            _uiState.value.copy(
                isEditing = true,
                ime = user.ime,
                prezime = user.prezime,
                email = user.email,
                datumRodjenja =
                    user.datumRodjenja,
                errorMessage = null,
                successMessage = null
            )
    }

    fun cancelEdit() {

        val user =
            _uiState.value.user
                ?: return

        _uiState.value =
            _uiState.value.copy(
                isEditing = false,
                ime = user.ime,
                prezime = user.prezime,
                email = user.email,
                datumRodjenja =
                    user.datumRodjenja,
                errorMessage = null
            )
    }

    fun onImeChange(value: String) {

        _uiState.value =
            _uiState.value.copy(
                ime = value,
                errorMessage = null,
                successMessage = null
            )
    }

    fun onPrezimeChange(value: String) {

        _uiState.value =
            _uiState.value.copy(
                prezime = value,
                errorMessage = null,
                successMessage = null
            )
    }

    fun onEmailChange(value: String) {

        _uiState.value =
            _uiState.value.copy(
                email = value,
                errorMessage = null,
                successMessage = null
            )
    }

    fun onDatumRodjenjaChange(
        value: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                datumRodjenja = value,
                errorMessage = null,
                successMessage = null
            )
    }

    fun saveProfile() {

        if (_uiState.value.isSaving) {
            return
        }

        val ime =
            _uiState.value.ime.trim()

        val prezime =
            _uiState.value.prezime.trim()

        val email =
            _uiState.value.email.trim()

        val datumRodjenja =
            _uiState.value.datumRodjenja.trim()

        val validationError =
            validate(
                ime = ime,
                prezime = prezime,
                email = email,
                datumRodjenja =
                    datumRodjenja
            )

        if (validationError != null) {

            _uiState.value =
                _uiState.value.copy(
                    errorMessage =
                        validationError
                )

            return
        }

        _uiState.value =
            _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                successMessage = null
            )

        viewModelScope.launch {

            val request =
                UpdateKorisnikRequest(
                    ime = ime,
                    prezime = prezime,
                    email = email,
                    datumRodjenja =
                        datumRodjenja
                )

            when (
                val result =
                    repository.updateProfile(
                        request
                    )
            ) {

                is ProfileResult.Success -> {

                    _uiState.value =
                        ProfileUiState(
                            user = result.user,

                            ime = result.user.ime,
                            prezime =
                                result.user.prezime,
                            email =
                                result.user.email,
                            datumRodjenja =
                                result.user.datumRodjenja,

                            successMessage =
                                "Profil je uspešno izmenjen."
                        )
                }

                is ProfileResult.Error -> {

                    _uiState.value =
                        _uiState.value.copy(
                            isSaving = false,
                            errorMessage =
                                result.message
                        )
                }

                ProfileResult.Unauthorized -> {

                    _uiState.value =
                        ProfileUiState(
                            sessionExpired = true
                        )
                }
            }
        }
    }

    private fun validate(
        ime: String,
        prezime: String,
        email: String,
        datumRodjenja: String
    ): String? {

        if (
            ime.isBlank() ||
            prezime.isBlank() ||
            email.isBlank() ||
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
            ProfileUiState()
    }
}