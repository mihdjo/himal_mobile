package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import kotlinx.coroutines.flow.first

class FeedRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getFeed(): FeedResult {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return FeedResult.Unauthorized
            }

            val response =
                api.getExpeditions(
                    authorization = "Bearer $token"
                )

            if (response.isSuccessful) {

                FeedResult.Success(
                    response.body().orEmpty()
                )

            } else if (response.code() == 401) {

                sessionManager.clearToken()
                FeedResult.Unauthorized

            } else {

                val errorBody =
                    response.errorBody()?.string()

                val message =
                    try {
                        gson.fromJson(
                            errorBody,
                            ApiErrorResponse::class.java
                        )?.message
                            ?: "Nije moguće učitati ekspedicije."
                    } catch (_: Exception) {
                        "Nije moguće učitati ekspedicije."
                    }

                FeedResult.Error(message)
            }

        } catch (e: Exception) {

            FeedResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }
}