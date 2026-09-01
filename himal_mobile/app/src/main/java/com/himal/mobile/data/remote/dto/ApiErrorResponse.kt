package com.himal.mobile.data.remote.dto

data class ApiErrorResponse(
    val status: Int,
    val error: String?,
    val message: String?,
    val path: String?
)