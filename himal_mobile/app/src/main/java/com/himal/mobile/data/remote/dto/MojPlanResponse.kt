package com.himal.mobile.data.remote.dto

data class MojPlanResponse(
    val ekspedicija: EkspedicijaResponse,
    val status: Boolean,
    val datumDodavanja: String
)