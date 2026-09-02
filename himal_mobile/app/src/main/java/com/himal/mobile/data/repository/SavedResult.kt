package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

sealed class SavedResult {

    data class Success(
        val expeditions: List<EkspedicijaResponse>
    ) : SavedResult()

    data class Error(
        val message: String
    ) : SavedResult()

    data object Unauthorized : SavedResult()
}