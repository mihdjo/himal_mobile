package com.himal.mobile.data.repository

import com.google.gson.Gson
import com.himal.mobile.data.local.SessionManager
import com.himal.mobile.data.remote.HimalApiService
import com.himal.mobile.data.remote.dto.ApiErrorResponse
import com.himal.mobile.data.remote.dto.LoginRequest
import kotlinx.coroutines.flow.first
import com.himal.mobile.data.remote.dto.RegisterRequest

class AuthRepository(
    private val api: HimalApiService,
    private val sessionManager: SessionManager
) {

    private val gson = Gson()

    suspend fun login(
        username: String,
        password: String
    ): LoginResult {

        return try {

            val response = api.login(
                LoginRequest(
                    username = username,
                    password = password
                )
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body == null) {
                    LoginResult.Error(
                        "Server je vratio prazan odgovor."
                    )
                } else {

                    sessionManager.saveToken(body.token)

                    LoginResult.Success(body)
                }

            } else {

                val errorBody =
                    response.errorBody()?.string()

                val message =
                    try {
                        gson.fromJson(
                            errorBody,
                            ApiErrorResponse::class.java
                        )?.message
                            ?: "Prijava nije uspela."
                    } catch (_: Exception) {
                        "Prijava nije uspela."
                    }

                LoginResult.Error(message)
            }

        } catch (e: Exception) {

            LoginResult.Error(
                e.message
                    ?: "Nije moguće povezati se sa serverom."
            )
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): RegisterResult {

        return try {

            val response =
                api.register(request)

            if (response.isSuccessful) {

                val user = response.body()

                if (user != null) {

                    RegisterResult.Success(user)

                } else {

                    RegisterResult.Error(
                        "Server nije vratio podatke novog korisnika."
                    )
                }

            } else {

                val message =
                    try {

                        gson.fromJson(
                            response.errorBody()?.string(),
                            ApiErrorResponse::class.java
                        )?.message
                            ?: "Registracija nije uspela."

                    } catch (_: Exception) {

                        "Registracija nije uspela."
                    }

                RegisterResult.Error(message)
            }

        } catch (e: Exception) {

            RegisterResult.Error(
                e.message
                    ?: "Greška prilikom povezivanja sa serverom."
            )
        }
    }

    suspend fun getSavedToken(): String? {
        return sessionManager.token.first()
    }

    suspend fun logout() {
        sessionManager.clearToken()
    }
}