package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.TipEkspedicijeResponse

sealed class ExpeditionTypesResult {

    data class Success(
        val types: List<TipEkspedicijeResponse>
    ) : ExpeditionTypesResult()

    data class Error(
        val message: String
    ) : ExpeditionTypesResult()

    data object Unauthorized : ExpeditionTypesResult()
}