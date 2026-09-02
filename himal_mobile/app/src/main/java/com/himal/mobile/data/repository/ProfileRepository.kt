package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.UpdateKorisnikRequest
import kotlinx.coroutines.flow.first

class ProfileRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun getProfile(): ProfileResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ProfileResult.Unauthorized
            }

            val response =
                api.getCurrentUser(
                    authorization = "Bearer $token"
                )

            handleResponse(response)

        } catch (e: Exception) {

            ProfileResult.Error(
                e.message
                    ?: "Greška prilikom učitavanja profila."
            )
        }
    }

    suspend fun updateProfile(
        request: UpdateKorisnikRequest
    ): ProfileResult {

        return try {

            val token = sessionManager.token.first()

            if (token.isNullOrBlank()) {
                return ProfileResult.Unauthorized
            }

            val response =
                api.updateCurrentUser(
                    authorization = "Bearer $token",
                    request = request
                )

            handleResponse(response)

        } catch (e: Exception) {

            ProfileResult.Error(
                e.message
                    ?: "Greška prilikom izmene profila."
            )
        }
    }

    private suspend fun handleResponse(
        response: retrofit2.Response<
                com.himal.mobile.data.remote.dto.KorisnikResponse
                >
    ): ProfileResult {

        return when {

            response.isSuccessful -> {

                val user = response.body()

                if (user != null) {

                    ProfileResult.Success(user)

                } else {

                    ProfileResult.Error(
                        "Server nije vratio podatke korisnika."
                    )
                }
            }

            response.code() == 401 -> {

                sessionManager.clearToken()

                ProfileResult.Unauthorized
            }

            else -> {

                ProfileResult.Error(
                    readErrorMessage(
                        response.errorBody()?.string(),
                        "Nije moguće izvršiti zahtev."
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