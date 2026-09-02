package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import kotlinx.coroutines.flow.first

class PlanRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getPlan(): PlanResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return PlanResult.Unauthorized
            }

            val response =
                api.getMyPlan(
                    authorization = "Bearer $token"
                )

            when {

                response.isSuccessful -> {

                    PlanResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    PlanResult.Unauthorized
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
                                ?: "Nije moguće učitati Moj plan."

                        } catch (_: Exception) {

                            "Nije moguće učitati Moj plan."
                        }

                    PlanResult.Error(message)
                }
            }

        } catch (e: Exception) {

            PlanResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }
}