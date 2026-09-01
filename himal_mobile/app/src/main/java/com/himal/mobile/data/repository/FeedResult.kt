package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.EkspedicijaResponse

sealed class FeedResult {

    data class Success(
        val expeditions: List<EkspedicijaResponse>
    ) : FeedResult()

    data class Error(
        val message: String
    ) : FeedResult()

    data object Unauthorized : FeedResult()
}