package com.himal.mobile.ui.feed

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

data class FeedUiState(
    val isLoading: Boolean = false,
    val expeditions: List<EkspedicijaResponse> = emptyList(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)