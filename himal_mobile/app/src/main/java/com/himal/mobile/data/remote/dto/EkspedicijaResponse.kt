package com.himal.mobile.data.remote.dto

data class EkspedicijaResponse(
    val idEkspedicije: Long,
    val naziv: String,
    val opis: String,
    val datumPolaska: String,
    val lokacija: String,
    val tezina: String,
    val trajanjeMin: Int,
    val duzinaKm: Double,
    val externalUrl: String?,
    val datumKreiranja: String,
    val idTipEkspedicije: Long,
    val tipEkspedicije: String,
    val idAutora: Long,
    val autorUsername: String
)