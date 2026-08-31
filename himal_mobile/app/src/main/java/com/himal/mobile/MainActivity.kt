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

                        val response =
                            RetrofitClient.api.login(
                                LoginRequest(
                                    username = "alex",
                                    password = "alex123"
                                )
                            )

                        if (response.isSuccessful) {

                            val loginResponse =
                                response.body()

                            Log.d(
                                "HIMAL_API",
                                "LOGIN SUCCESS: " +
                                        "status=${response.code()}, " +
                                        "username=${loginResponse?.username}, " +
                                        "tokenReceived=${!loginResponse?.token.isNullOrBlank()}"
                            )

                            message =
                                "Login uspešan!\n" +
                                        "Korisnik: ${loginResponse?.username}\n" +
                                        "HTTP: ${response.code()}"

                        } else {

                            val errorBody =
                                response.errorBody()?.string()

                            Log.e(
                                "HIMAL_API",
                                "LOGIN ERROR: " +
                                        "status=${response.code()}, " +
                                        "body=$errorBody"
                            )

                            message =
                                "Login neuspešan\n" +
                                        "HTTP: ${response.code()}"
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