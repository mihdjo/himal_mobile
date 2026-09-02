package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

sealed class MyExpeditionsResult {

    data class Success(
        val expeditions: List<EkspedicijaResponse>
    ) : MyExpeditionsResult()

    data class Error(
        val message: String
    ) : MyExpeditionsResult()

    data object Unauthorized : MyExpeditionsResult()
}