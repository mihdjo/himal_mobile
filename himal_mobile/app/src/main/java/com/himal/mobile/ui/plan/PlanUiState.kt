package com.himal.mobile.ui.plan

import com.himal.mobile.data.remote.dto.MojPlanResponse

data class PlanUiState(
    val isLoading: Boolean = false,
    val items: List<MojPlanResponse> = emptyList(),
    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)