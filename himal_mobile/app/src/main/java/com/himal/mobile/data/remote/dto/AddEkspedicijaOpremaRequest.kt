package com.himal.mobile.data.remote.dto

data class AddEkspedicijaOpremaRequest(
    val idOpreme: Long,
    val obavezna: Boolean,
    val kolicina: Int,
    val napomena: String?
)