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

    suspend fun getMembershipStatus(
        expeditionId: Long
    ): ExpeditionMembershipResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionMembershipResult.Unauthorized
            }

            val authorization = "Bearer $token"

            val savedResponse =
                api.getSavedExpeditions(authorization)

            if (savedResponse.code() == 401) {

                sessionManager.clearToken()

                return ExpeditionMembershipResult.Unauthorized
            }

            if (!savedResponse.isSuccessful) {

                return ExpeditionMembershipResult.Error(
                    readErrorMessage(
                        savedResponse.errorBody()?.string(),
                        "Nije moguće proveriti sačuvane ekspedicije."
                    )
                )
            }

            val planResponse =
                api.getMyPlan(authorization)

            if (planResponse.code() == 401) {

                sessionManager.clearToken()

                return ExpeditionMembershipResult.Unauthorized
            }

            if (!planResponse.isSuccessful) {

                return ExpeditionMembershipResult.Error(
                    readErrorMessage(
                        planResponse.errorBody()?.string(),
                        "Nije moguće proveriti Moj plan."
                    )
                )
            }

            val isSaved =
                savedResponse
                    .body()
                    .orEmpty()
                    .any {
                        it.idEkspedicije == expeditionId
                    }

            val isInPlan =
                planResponse
                    .body()
                    .orEmpty()
                    .any {
                        it.ekspedicija.idEkspedicije ==
                                expeditionId
                    }

            ExpeditionMembershipResult.Success(
                isSaved = isSaved,
                isInPlan = isInPlan
            )

        } catch (e: Exception) {

            ExpeditionMembershipResult.Error(
                e.message
                    ?: "Greška prilikom provere ekspedicije."
            )
        }
    }

    suspend fun saveExpedition(
        expeditionId: Long
    ): ActionResult {

        return performAction(
            action = { authorization ->

                api.saveExpedition(
                    id = expeditionId,
                    authorization = authorization
                )
            },
            fallbackMessage =
                "Nije moguće sačuvati ekspediciju."
        )
    }

    suspend fun removeSavedExpedition(
        expeditionId: Long
    ): ActionResult {

        return performAction(
            action = { authorization ->

                api.removeSavedExpedition(
                    id = expeditionId,
                    authorization = authorization
                )
            },
            fallbackMessage =
                "Nije moguće ukloniti sačuvanu ekspediciju."
        )
    }

    suspend fun addToPlan(
        expeditionId: Long
    ): ActionResult {

        return performAction(
            action = { authorization ->

                api.addToPlan(
                    id = expeditionId,
                    authorization = authorization
                )
            },
            fallbackMessage =
                "Nije moguće dodati ekspediciju u Moj plan."
        )
    }

    suspend fun removeFromPlan(
        expeditionId: Long
    ): ActionResult {

        return performAction(
            action = { authorization ->

                api.removeFromPlan(
                    id = expeditionId,
                    authorization = authorization
                )
            },
            fallbackMessage =
                "Nije moguće ukloniti ekspediciju iz plana."
        )
    }

    private suspend fun performAction(
        action: suspend (String) -> retrofit2.Response<Unit>,
        fallbackMessage: String
    ): ActionResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ActionResult.Unauthorized
            }

            val response =
                action("Bearer $token")

            when {

                response.isSuccessful -> {
                    ActionResult.Success
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    ActionResult.Unauthorized
                }

                else -> {

                    ActionResult.Error(
                        readErrorMessage(
                            response.errorBody()?.string(),
                            fallbackMessage
                        )
                    )
                }
            }

        } catch (e: Exception) {

            ActionResult.Error(
                e.message ?: fallbackMessage
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