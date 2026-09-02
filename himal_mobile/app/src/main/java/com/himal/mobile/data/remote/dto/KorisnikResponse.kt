package com.himal.mobile.data.remote.dto

data class KorisnikResponse(
    val idKorisnika: Long,
    val ime: String,
    val prezime: String,
    val email: String,
    val username: String,
    val datumRodjenja: String,
    val datumKreiranja: String
)