package com.himal.mobile.ui.profile

import com.himal.mobile.data.remote.dto.KorisnikResponse

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: KorisnikResponse? = null,

    val isEditing: Boolean = false,
    val isSaving: Boolean = false,

    val ime: String = "",
    val prezime: String = "",
    val email: String = "",
    val datumRodjenja: String = "",

    val errorMessage: String? = null,
    val successMessage: String? = null,

    val sessionExpired: Boolean = false
)