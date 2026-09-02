package com.himal.mobile.ui.myexpeditions

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

data class MyExpeditionsUiState(
    val isLoading: Boolean = false,
    val expeditions: List<EkspedicijaResponse> = emptyList(),
    val deletingId: Long? = null,
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)