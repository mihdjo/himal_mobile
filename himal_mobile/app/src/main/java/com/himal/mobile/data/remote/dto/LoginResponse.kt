package com.himal.mobile.data.remote.dto

data class LoginResponse(
    val token: String,
    val idKorisnika: Long,
    val username: String
)