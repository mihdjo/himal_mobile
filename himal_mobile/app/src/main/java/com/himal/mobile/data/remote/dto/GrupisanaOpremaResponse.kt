package com.himal.mobile.data.remote.dto

data class GrupisanaOpremaResponse(
    val idEkspedicije: Long,
    val nazivEkspedicije: String,
    val status: Boolean,
    val oprema: List<EkspedicijaOpremaResponse>
)