package com.himal.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.himal.mobile.ui.auth.AuthViewModel
import com.himal.mobile.ui.auth.LoginScreen
import com.himal.mobile.ui.theme.HimalTheme

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel =
            ViewModelProvider(this)[
                AuthViewModel::class.java
            ]

        setContent {

            HimalTheme {

                LoginScreen(
                    viewModel = authViewModel
                )
            }
        }
    }
}