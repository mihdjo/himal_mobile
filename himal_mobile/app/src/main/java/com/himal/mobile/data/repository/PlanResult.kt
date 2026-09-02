package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.MojPlanResponse

sealed class PlanResult {

    data class Success(
        val items: List<MojPlanResponse>
    ) : PlanResult()

    data class Error(
        val message: String
    ) : PlanResult()

    data object Unauthorized : PlanResult()
}