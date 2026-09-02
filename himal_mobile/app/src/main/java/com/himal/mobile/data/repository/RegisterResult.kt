package com.himal.mobile.data.repository

import com.himal.mobile.data.remote.dto.RegisterResponse

sealed class RegisterResult {

    data class Success(
        val user: RegisterResponse
    ) : RegisterResult()

    data class Error(
        val message: String
    ) : RegisterResult()
}