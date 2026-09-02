package com.himal.mobile.data.remote.dto

data class RegisterRequest(
    val ime: String,
    val prezime: String,
    val email: String,
    val username: String,
    val password: String,
    val datumRodjenja: String
)