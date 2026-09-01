package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.LoginResponse

sealed class LoginResult {

    data class Success(
        val data: LoginResponse
    ) : LoginResult()

    data class Error(
        val message: String
    ) : LoginResult()
}