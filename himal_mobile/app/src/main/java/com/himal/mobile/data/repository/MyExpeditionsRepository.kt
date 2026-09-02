package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.EkspedicijaRequest
import kotlinx.coroutines.flow.first

class MyExpeditionsRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getMyExpeditions(): MyExpeditionsResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return MyExpeditionsResult.Unauthorized
            }

            val response =
                api.getMyExpeditions(
                    "Bearer $token"
                )

            when {

                response.isSuccessful -> {

                    MyExpeditionsResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    MyExpeditionsResult.Unauthorized
                }

                else -> {

                    MyExpeditionsResult.Error(
                        readErrorMessage(
                            response.errorBody()?.string(),
                            "Nije moguće učitati tvoje ekspedicije."
                        )
                    )
                }
            }

        } catch (e: Exception) {

            MyExpeditionsResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }

    suspend fun getExpedition(
        id: Long
    ): ExpeditionCrudResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionCrudResult.Unauthorized
            }

            val response =
                api.getExpeditionById(
                    id = id,
                    authorization = "Bearer $token"
                )

            handleExpeditionResponse(response)

        } catch (e: Exception) {

            ExpeditionCrudResult.Error(
                e.message ?: "Nije moguće učitati ekspediciju."
            )
        }
    }

    suspend fun create(
        request: EkspedicijaRequest
    ): ExpeditionCrudResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionCrudResult.Unauthorized
            }

            val response =
                api.createExpedition(
                    authorization = "Bearer $token",
                    request = request
                )

            handleExpeditionResponse(response)

        } catch (e: Exception) {

            ExpeditionCrudResult.Error(
                e.message ?: "Nije moguće kreirati ekspediciju."
            )
        }
    }

    suspend fun update(
        id: Long,
        request: EkspedicijaRequest
    ): ExpeditionCrudResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionCrudResult.Unauthorized
            }

            val response =
                api.updateExpedition(
                    id = id,
                    authorization = "Bearer $token",
                    request = request
                )

            handleExpeditionResponse(response)

        } catch (e: Exception) {

            ExpeditionCrudResult.Error(
                e.message ?: "Nije moguće izmeniti ekspediciju."
            )
        }
    }

    suspend fun delete(
        id: Long
    ): ActionResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ActionResult.Unauthorized
            }

            val response =
                api.deleteExpedition(
                    id = id,
                    authorization = "Bearer $token"
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
                            response.errorBody()?.string(),
                            "Nije moguće obrisati ekspediciju."
                        )
                    )
                }
            }

        } catch (e: Exception) {

            ActionResult.Error(
                e.message
                    ?: "Nije moguće obrisati ekspediciju."
            )
        }
    }

    private suspend fun handleExpeditionResponse(
        response: retrofit2.Response<
                com.himal.mobile.data.remote.dto.EkspedicijaResponse
                >
    ): ExpeditionCrudResult {

        return when {

            response.isSuccessful -> {

                val expedition = response.body()

                if (expedition != null) {

                    ExpeditionCrudResult.Success(
                        expedition
                    )

                } else {

                    ExpeditionCrudResult.Error(
                        "Server nije vratio ekspediciju."
                    )
                }
            }

            response.code() == 401 -> {

                sessionManager.clearToken()

                ExpeditionCrudResult.Unauthorized
            }

            else -> {

                ExpeditionCrudResult.Error(
                    readErrorMessage(
                        response.errorBody()?.string(),
                        "Zahtev nije uspeo."
                    )
                )
            }
        }
    }

    suspend fun getExpeditionTypes(): ExpeditionTypesResult {

        return try {

            val token =
                sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ExpeditionTypesResult.Unauthorized
            }

            val response =
                api.getExpeditionTypes(
                    authorization = "Bearer $token"
                )

            when {

                response.isSuccessful -> {

                    ExpeditionTypesResult.Success(
                        response.body().orEmpty()
                    )
                }

                response.code() == 401 -> {

                    sessionManager.clearToken()

                    ExpeditionTypesResult.Unauthorized
                }

                else -> {

                    ExpeditionTypesResult.Error(
                        readErrorMessage(
                            response.errorBody()?.string(),
                            "Nije moguće učitati tipove ekspedicija."
                        )
                    )
                }
            }

        } catch (e: Exception) {

            ExpeditionTypesResult.Error(
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