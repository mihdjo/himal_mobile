package com.himal.mobile.ui.feed

import com.himal.mobile.data.remote.dto.EkspedicijaResponse
import com.himal.mobile.data.remote.dto.TipEkspedicijeResponse

data class FeedUiState(
    val isLoading: Boolean = false,
    val expeditions: List<EkspedicijaResponse> = emptyList(),

    val search: String = "",
    val location: String = "",

    val difficulty: String? = null,

    val expeditionTypes:
    List<TipEkspedicijeResponse> = emptyList(),

    val selectedTypeId: Long? = null,

    val maxDuration: String = "",
    val maxDistance: String = "",

    val showFilters: Boolean = false,
    val isFiltering: Boolean = false,

    val errorMessage: String? = null,
    val sessionExpired: Boolean = false
)