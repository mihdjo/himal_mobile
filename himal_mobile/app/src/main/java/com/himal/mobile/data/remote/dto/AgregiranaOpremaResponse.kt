package com.himal.mobile.data.remote.dto

data class AgregiranaOpremaResponse(
    val idOpreme: Long,
    val naziv: String,
    val opis: String?,
    val ukupnaKolicina: Int,
    val obavezna: Boolean
)