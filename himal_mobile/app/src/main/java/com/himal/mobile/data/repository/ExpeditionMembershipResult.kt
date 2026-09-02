package com.himal.mobile.data.repository

sealed class ExpeditionMembershipResult {

    data class Success(
        val isSaved: Boolean,
        val isInPlan: Boolean
    ) : ExpeditionMembershipResult()

    data class Error(
        val message: String
    ) : ExpeditionMembershipResult()

    data object Unauthorized : ExpeditionMembershipResult()
}