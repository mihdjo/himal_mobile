package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.UpdateMojPlanStatusRequest
import kotlinx.coroutines.flow.first

class PackingListRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getPackingList(): PackingListResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return PackingListResult.Unauthorized
            }

            val authorization = "Bearer $token"

            // 1. AGGREGATED
            val aggregatedResponse =
                api.getAggregatedPlanEquipment(
                    authorization
                )

            if (aggregatedResponse.code() == 401) {

                sessionManager.clearToken()

                return PackingListResult.Unauthorized
            }

            if (!aggregatedResponse.isSuccessful) {

                return PackingListResult.Error(
                    readErrorMessage(
                        aggregatedResponse
                            .errorBody()
                            ?.string(),
                        "Nije moguće učitati packing listu."
                    )
                )
            }

            // 2. GROUPED
            val groupedResponse =
                api.getGroupedPlanEquipment(
                    authorization
                )

            if (groupedResponse.code() == 401) {

                sessionManager.clearToken()

                return PackingListResult.Unauthorized
            }

            if (!groupedResponse.isSuccessful) {

                return PackingListResult.Error(
                    readErrorMessage(
                        groupedResponse
                            .errorBody()
                            ?.string(),
                        "Nije moguće učitati grupisanu opremu."
                    )
                )
            }

            PackingListResult.Success(
                aggregated =
                    aggregatedResponse.body().orEmpty(),
                grouped =
                    groupedResponse.body().orEmpty()
            )

        } catch (e: Exception) {

            PackingListResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }

    suspend fun updatePlanStatus(
        expeditionId: Long,
        status: Boolean
    ): ActionResult {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ActionResult.Unauthorized
            }

            val response =
                api.updatePlanStatus(
                    id = expeditionId,
                    authorization = "Bearer $token",
                    request =
                        UpdateMojPlanStatusRequest(
                            status = status
                        )
                )

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
                            response
                                .errorBody()
                                ?.string(),
                            "Nije moguće promeniti status ekspedicije."
                        )
                    )
                }
            }

        } catch (e: Exception) {

            ActionResult.Error(
                e.message
                    ?: "Nije moguće promeniti status ekspedicije."
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