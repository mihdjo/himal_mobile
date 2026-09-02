package com.himal.mobile.data.remote.dto

data class UpdateKorisnikRequest(
    val ime: String,
    val prezime: String,
    val email: String,
    val datumRodjenja: String
)