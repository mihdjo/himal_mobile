package com.himal.mobile.data.remote

import com.himal.mobile.data.remote.dto.LoginRequest
import com.himal.mobile.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HimalApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}