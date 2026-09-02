package com.himal.mobile.ui.details

import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.EkspedicijaResponse

data class ExpeditionDetailsUiState(
    val isLoading: Boolean = false,
    val expedition: EkspedicijaResponse? = null,
    val equipment: List<EkspedicijaOpremaResponse> = emptyList(),

    val isSaved: Boolean = false,
    val isInPlan: Boolean = false,

    val isSavedLoading: Boolean = false,
    val isPlanLoading: Boolean = false,

    val errorMessage: String? = null,
    val equipmentErrorMessage: String? = null,
    val actionErrorMessage: String? = null,

    val sessionExpired: Boolean = false
)