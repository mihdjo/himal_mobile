package com.himal.mobile.ui.packing

import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse

data class PackingListUiState(
    val isLoading: Boolean = false,

    val aggregated:
    List<AgregiranaOpremaResponse> = emptyList(),

    val grouped:
    List<GrupisanaOpremaResponse> = emptyList(),

    val preparedItems:
    Set<String> = emptySet(),

    val viewMode: PackingViewMode =
        PackingViewMode.ALL,

    val errorMessage: String? = null,
    val checklistErrorMessage: String? = null,

    val sessionExpired: Boolean = false
)