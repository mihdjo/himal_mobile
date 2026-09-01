package com.himal.mobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.himal.mobile.data.remote.RetrofitClient
import com.himal.mobile.data.remote.dto.LoginRequest
import com.himal.mobile.ui.theme.HimalTheme
import com.himal.mobile.data.local.SessionManager
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            HimalTheme {

                var message by remember {
                    mutableStateOf("Testing HIMAL API...")
                }

                LaunchedEffect(Unit) {

                    try {

                        // 1. LOGIN
                        val sessionManager =
                            SessionManager(applicationContext)

                        val loginResponse =
                            RetrofitClient.api.login(
                                LoginRequest(
                                    username = "alex",
                                    password = "alex123"
                                )
                            )

                        if (!loginResponse.isSuccessful) {

                            val errorBody =
                                loginResponse.errorBody()?.string()

                            Log.e(
                                "HIMAL_API",
                                "LOGIN ERROR: " +
                                        "status=${loginResponse.code()}, " +
                                        "body=$errorBody"
                            )

                            message =
                                "Login neuspešan\n" +
                                        "HTTP: ${loginResponse.code()}"

                            return@LaunchedEffect
                        }

                        val loginBody = loginResponse.body()

                        if (loginBody == null) {

                            Log.e(
                                "HIMAL_API",
                                "LOGIN ERROR: empty body"
                            )

                            message = "Login response je prazan."

                            return@LaunchedEffect
                        }

                        sessionManager.saveToken(
                            loginBody.token
                        )

                        Log.d(
                            "HIMAL_DATASTORE",
                            "JWT saved to DataStore"
                        )

                        val savedToken =
                            sessionManager.token.first()

                        Log.d(
                            "HIMAL_DATASTORE",
                            "JWT loaded: ${!savedToken.isNullOrBlank()}"
                        )

                        Log.d(
                            "HIMAL_API",
                            "LOGIN SUCCESS: " +
                                    "username=${loginBody.username}, " +
                                    "tokenReceived=${loginBody.token.isNotBlank()}"
                        )

                        // 2. PROTECTED FEED
                        val feedResponse =
                            RetrofitClient.api.getExpeditions(
                                authorization =
                                    "Bearer $savedToken"
                            )

                        if (feedResponse.isSuccessful) {

                            val expeditions =
                                feedResponse.body().orEmpty()

                            Log.d(
                                "HIMAL_API",
                                "FEED SUCCESS: " +
                                        "status=${feedResponse.code()}, " +
                                        "count=${expeditions.size}"
                            )

                            expeditions.forEach { expedition ->

                                Log.d(
                                    "HIMAL_API",
                                    "EXPEDITION: " +
                                            "id=${expedition.idEkspedicije}, " +
                                            "naziv=${expedition.naziv}, " +
                                            "lokacija=${expedition.lokacija}, " +
                                            "tezina=${expedition.tezina}"
                                )
                            }

                            message =
                                "HIMAL API radi!\n" +
                                        "Login: ${loginBody.username}\n" +
                                        "Ekspedicija: ${expeditions.size}\n" +
                                        "Feed HTTP: ${feedResponse.code()}"

                        } else {

                            val errorBody =
                                feedResponse.errorBody()?.string()

                            Log.e(
                                "HIMAL_API",
                                "FEED ERROR: " +
                                        "status=${feedResponse.code()}, " +
                                        "body=$errorBody"
                            )

                            message =
                                "Feed neuspešan\n" +
                                        "HTTP: ${feedResponse.code()}"
                        }

                    } catch (e: Exception) {

                        Log.e(
                            "HIMAL_API",
                            "NETWORK ERROR",
                            e
                        )

                        message =
                            "Network error:\n${e.message}"
                    }
                }

                Text(text = message)
            }
        }
    }
}