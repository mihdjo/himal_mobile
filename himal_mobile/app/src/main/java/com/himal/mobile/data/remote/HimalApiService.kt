package com.himal.mobile.data.remote

import com.himal.mobile.data.remote.dto.EkspedicijaResponse
import com.himal.mobile.data.remote.dto.LoginRequest
import com.himal.mobile.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface HimalApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/expeditions")
    suspend fun getExpeditions(
        @Header("Authorization") authorization: String
    ): Response<List<EkspedicijaResponse>>
}