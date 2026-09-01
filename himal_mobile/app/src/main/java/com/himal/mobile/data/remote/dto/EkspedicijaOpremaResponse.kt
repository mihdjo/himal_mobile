package com.himal.mobile.data.remote.dto

data class EkspedicijaOpremaResponse(
    val idOpreme: Long,
    val naziv: String,
    val opis: String?,
    val obavezna: Boolean,
    val kolicina: Int,
    val napomena: String?
)