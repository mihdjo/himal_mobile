package com.himal.mobile.data.remote.dto

data class EkspedicijaRequest(
    val naziv: String,
    val opis: String,
    val datumPolaska: String,
    val lokacija: String,
    val tezina: String,
    val trajanjeMin: Int,
    val duzinaKm: Double,
    val externalUrl: String?,
    val idTipEkspedicije: Long
)