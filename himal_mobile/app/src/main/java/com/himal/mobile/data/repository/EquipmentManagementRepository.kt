package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.AddEkspedicijaOpremaRequest
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.CreateOpremaRequest
import com.himal.mobile.data.remote.dto.EkspedicijaOpremaResponse
import com.himal.mobile.data.remote.dto.OpremaResponse
import com.himal.mobile.data.remote.dto.UpdateEkspedicijaOpremaRequest
import kotlinx.coroutines.flow.first
import retrofit2.Response

class EquipmentManagementRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getCatalog():
            EquipmentManagementResult<List<OpremaResponse>> {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            val response =
                api.getEquipmentCatalog(
                    authorization = "Bearer $token"
                )

            handleResponse(
                response,
                "Nije moguće učitati katalog opreme."
            )

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće učitati katalog opreme."
            )
        }
    }

    suspend fun getExpeditionEquipment(
        expeditionId: Long
    ): EquipmentManagementResult<
            List<EkspedicijaOpremaResponse>
            > {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            /*
             * Ako tvoja postojeća GET metoda nema Authorization,
             * token ovde ipak proveravamo zbog session-a,
             * a poziv ostaje bez headera.
             */
            val response =
                api.getExpeditionEquipment(
                    id = expeditionId,
                    authorization = "Bearer $token"
                )

            handleResponse(
                response,
                "Nije moguće učitati opremu ekspedicije."
            )

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće učitati opremu ekspedicije."
            )
        }
    }

    suspend fun createEquipment(
        request: CreateOpremaRequest
    ): EquipmentManagementResult<OpremaResponse> {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            val response =
                api.createEquipment(
                    authorization = "Bearer $token",
                    request = request
                )

            handleResponse(
                response,
                "Nije moguće kreirati opremu."
            )

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće kreirati opremu."
            )
        }
    }

    suspend fun addEquipment(
        expeditionId: Long,
        request: AddEkspedicijaOpremaRequest
    ): EquipmentManagementResult<
            EkspedicijaOpremaResponse
            > {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            val response =
                api.addExpeditionEquipment(
                    expeditionId = expeditionId,
                    authorization = "Bearer $token",
                    request = request
                )

            handleResponse(
                response,
                "Nije moguće dodati opremu."
            )

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće dodati opremu."
            )
        }
    }

    suspend fun updateEquipment(
        expeditionId: Long,
        equipmentId: Long,
        request: UpdateEkspedicijaOpremaRequest
    ): EquipmentManagementResult<
            EkspedicijaOpremaResponse
            > {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            val response =
                api.updateExpeditionEquipment(
                    expeditionId = expeditionId,
                    equipmentId = equipmentId,
                    authorization = "Bearer $token",
                    request = request
                )

            handleResponse(
                response,
                "Nije moguće izmeniti opremu."
            )

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće izmeniti opremu."
            )
        }
    }

    suspend fun deleteEquipment(
        expeditionId: Long,
        equipmentId: Long
    ): EquipmentManagementResult<Unit> {

        return try {

            val token = getToken()
                ?: return EquipmentManagementResult.Unauthorized

            val response =
                api.deleteExpeditionEquipment(
                    expeditionId = expeditionId,
                    equipmentId = equipmentId,
                    authorization = "Bearer $token"
                )

            if (response.isSuccessful) {

                EquipmentManagementResult.Success(
                    Unit
                )

            } else if (response.code() == 401) {

                sessionManager.clearToken()

                EquipmentManagementResult.Unauthorized

            } else {

                EquipmentManagementResult.Error(
                    readErrorMessage(
                        response.errorBody()?.string(),
                        "Nije moguće ukloniti opremu."
                    )
                )
            }

        } catch (e: Exception) {

            EquipmentManagementResult.Error(
                e.message
                    ?: "Nije moguće ukloniti opremu."
            )
        }
    }

    private suspend fun getToken(): String? {

        return sessionManager.token.first()
    }

    private suspend fun <T> handleResponse(
        response: Response<T>,
        fallback: String
    ): EquipmentManagementResult<T> {

        return when {

            response.isSuccessful -> {

                val body = response.body()

                if (body != null) {

                    EquipmentManagementResult.Success(
                        body
                    )

                } else {

                    EquipmentManagementResult.Error(
                        fallback
                    )
                }
            }

            response.code() == 401 -> {

                sessionManager.clearToken()

                EquipmentManagementResult.Unauthorized
            }

            else -> {

                EquipmentManagementResult.Error(
                    readErrorMessage(
                        response.errorBody()?.string(),
                        fallback
                    )
                )
            }
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