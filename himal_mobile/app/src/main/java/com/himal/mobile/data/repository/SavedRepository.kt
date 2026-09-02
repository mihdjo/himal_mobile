package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import kotlinx.coroutines.flow.first

class SavedRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getSavedExpeditions(): SavedResult {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return SavedResult.Unauthorized
            }

            val response =
                api.getSavedExpeditions(
                    authorization = "Bearer $token"
                )

            when {

                response.isSuccessful -> {

                    SavedResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    SavedResult.Unauthorized
                }

                else -> {

                    val errorBody =
                        response.errorBody()?.string()

                    val message =
                        try {

                            gson.fromJson(
                                errorBody,
                                ApiErrorResponse::class.java
                            )?.message
                                ?: "Nije moguće učitati sačuvane ekspedicije."

                        } catch (_: Exception) {

                            "Nije moguće učitati sačuvane ekspedicije."
                        }

                    SavedResult.Error(message)
                }
            }

        } catch (e: Exception) {

            SavedResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }
}