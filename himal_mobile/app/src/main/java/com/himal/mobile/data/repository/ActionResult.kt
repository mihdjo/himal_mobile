package com.himal.mobile.data.repository

sealed class ActionResult {

    data object Success : ActionResult()

    data class Error(
        val message: String
    ) : ActionResult()

    data object Unauthorized : ActionResult()
}