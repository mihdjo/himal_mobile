package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.KorisnikResponse

sealed class ProfileResult {

    data class Success(
        val user: KorisnikResponse
    ) : ProfileResult()

    data class Error(
        val message: String
    ) : ProfileResult()

    data object Unauthorized : ProfileResult()
}