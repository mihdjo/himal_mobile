package com.himal.mobile.ui.saved

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

data class SavedUiState(
    val isLoading: Boolean = false,
    val expeditions: List<EkspedicijaResponse> = emptyList(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)