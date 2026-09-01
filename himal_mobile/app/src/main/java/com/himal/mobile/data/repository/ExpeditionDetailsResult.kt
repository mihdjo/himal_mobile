package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

sealed class ExpeditionDetailsResult {

    data class Success(
        val expedition: EkspedicijaResponse
    ) : ExpeditionDetailsResult()

    data class Error(
        val message: String
    ) : ExpeditionDetailsResult()

    data object Unauthorized : ExpeditionDetailsResult()
}