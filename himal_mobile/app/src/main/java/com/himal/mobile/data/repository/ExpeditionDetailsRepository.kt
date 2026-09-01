package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import kotlinx.coroutines.flow.first

class ExpeditionDetailsRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getExpedition(
        id: Long
    ): ExpeditionDetailsResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionDetailsResult.Unauthorized
            }

            val response =
                api.getExpeditionById(
                    id = id,
                    authorization = "Bearer $token"
                )

            if (response.isSuccessful) {

                val body = response.body()

                if (body == null) {

                    ExpeditionDetailsResult.Error(
                        "Server je vratio prazan odgovor."
                    )

                } else {

                    ExpeditionDetailsResult.Success(body)
                }

            } else if (response.code() == 401) {

                sessionManager.clearToken()
                ExpeditionDetailsResult.Unauthorized

            } else {

                val errorBody =
                    response.errorBody()?.string()

                val message =
                    try {
                        gson.fromJson(
                            errorBody,
                            ApiErrorResponse::class.java
                        )?.message
                            ?: "Nije moguće učitati ekspediciju."
                    } catch (_: Exception) {
                        "Nije moguće učitati ekspediciju."
                    }

                ExpeditionDetailsResult.Error(message)
            }

        } catch (e: Exception) {

            ExpeditionDetailsResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }

    suspend fun getEquipment(
        expeditionId: Long
    ): EquipmentResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return EquipmentResult.Unauthorized
            }

            val response =
                api.getExpeditionEquipment(
                    id = expeditionId,
                    authorization = "Bearer $token"
                )

            if (response.isSuccessful) {

                EquipmentResult.Success(
                    response.body().orEmpty()
                )

            } else if (response.code() == 401) {

                sessionManager.clearToken()

                EquipmentResult.Unauthorized

            } else {

                val errorBody =
                    response.errorBody()?.string()

                val message =
                    try {
                        gson.fromJson(
                            errorBody,
                            ApiErrorResponse::class.java
                        )?.message
                            ?: "Nije moguće učitati opremu."
                    } catch (_: Exception) {
                        "Nije moguće učitati opremu."
                    }

                EquipmentResult.Error(message)
            }

        } catch (e: Exception) {

            EquipmentResult.Error(
                e.message
                    ?: "Greška prilikom učitavanja opreme."
            )
        }
    }
}