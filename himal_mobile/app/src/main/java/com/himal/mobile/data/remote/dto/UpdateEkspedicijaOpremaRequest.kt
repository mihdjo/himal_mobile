package com.himal.mobile.data.remote.dto

data class UpdateEkspedicijaOpremaRequest(
    val obavezna: Boolean,
    val kolicina: Int,
    val napomena: String?
)