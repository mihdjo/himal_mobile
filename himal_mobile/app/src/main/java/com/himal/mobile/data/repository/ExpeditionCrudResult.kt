package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

sealed class ExpeditionCrudResult {

    data class Success(
        val expedition: EkspedicijaResponse
    ) : ExpeditionCrudResult()

    data class Error(
        val message: String
    ) : ExpeditionCrudResult()

    data object Unauthorized : ExpeditionCrudResult()
}