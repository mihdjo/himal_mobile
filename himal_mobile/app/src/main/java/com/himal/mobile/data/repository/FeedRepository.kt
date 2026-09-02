package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.TipEkspedicijeResponse
import kotlinx.coroutines.flow.first

class FeedRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getFeed(
        search: String? = null,
        location: String? = null,
        difficulty: String? = null,
        typeId: Long? = null,
        maxDuration: Int? = null,
        maxDistance: Double? = null
    ): FeedResult {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return FeedResult.Unauthorized
            }

            val response =
                api.getExpeditions(
                    authorization =
                        "Bearer $token",

                    search =
                        search,

                    location =
                        location,

                    difficulty =
                        difficulty,

                    typeId =
                        typeId,

                    maxDuration =
                        maxDuration,

                    maxDistance =
                        maxDistance
                )

            when {

                response.isSuccessful -> {

                    FeedResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    FeedResult.Unauthorized
                }

                else -> {

                    FeedResult.Error(
                        readErrorMessage(
                            response.errorBody()
                                ?.string(),
                            "Nije moguće učitati ekspedicije."
                        )
                    )
                }
            }

        } catch (e: Exception) {

            FeedResult.Error(
                e.message
                    ?: "Greška prilikom učitavanja ekspedicija."
            )
        }
    }

    suspend fun getExpeditionTypes():
            EquipmentManagementResult<List<TipEkspedicijeResponse>> {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return EquipmentManagementResult.Unauthorized
            }

            val response =
                api.getExpeditionTypes(
                    authorization =
                        "Bearer $token"
                )

            when {

                response.isSuccessful -> {

                    EquipmentManagementResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    EquipmentManagementResult.Unauthorized
                }

                else -> {

                    EquipmentManagementResult.Error(
                        "Nije moguće učitati tipove ekspedicija."
                    )
                }
            }

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće učitati tipove ekspedicija."
            )
        }
    }

    private fun readErrorMessage(
        errorBody: String?,
        fallback: String
    ): String {

        return try {

            gson.fromJson(
                errorBody,
                ApiErrorResponse::class.java
            )?.message ?: fallback

        } catch (_: Exception) {

            fallback
        }
    }
}