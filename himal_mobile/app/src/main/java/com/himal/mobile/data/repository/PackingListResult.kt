package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.AgregiranaOpremaResponse
import com.himal.mobile.data.remote.dto.GrupisanaOpremaResponse

sealed class PackingListResult {

    data class Success(
        val aggregated: List<AgregiranaOpremaResponse>,
        val grouped: List<GrupisanaOpremaResponse>
    ) : PackingListResult()

    data class Error(
        val message: String
    ) : PackingListResult()

    data object Unauthorized : PackingListResult()
}