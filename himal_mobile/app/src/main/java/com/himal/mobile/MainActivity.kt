package com.himal.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModelProvider
import com.himal.mobile.ui.auth.AuthViewModel
import com.himal.mobile.ui.auth.LoginScreen
import com.himal.mobile.ui.auth.RegisterScreen
import com.himal.mobile.ui.auth.RegisterViewModel
import com.himal.mobile.ui.details.ExpeditionDetailsViewModel
import com.himal.mobile.ui.feed.FeedViewModel
import com.himal.mobile.ui.navigation.HimalNavGraph
import com.himal.mobile.ui.packing.PackingListViewModel
import com.himal.mobile.ui.plan.PlanViewModel
import com.himal.mobile.ui.profile.ProfileViewModel
import com.himal.mobile.ui.saved.SavedViewModel
import com.himal.mobile.ui.theme.HimalTheme
import com.himal.mobile.ui.myexpeditions.MyExpeditionsViewModel
import com.himal.mobile.ui.expeditionform.ExpeditionFormViewModel

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var detailsViewModel: ExpeditionDetailsViewModel
    private lateinit var savedViewModel: SavedViewModel
    private lateinit var planViewModel: PlanViewModel
    private lateinit var packingListViewModel: PackingListViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var myExpeditionsViewModel: MyExpeditionsViewModel

    private lateinit var expeditionFormViewModel: ExpeditionFormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel =
            ViewModelProvider(this)[
                AuthViewModel::class.java
            ]

        feedViewModel =
            ViewModelProvider(this)[
                FeedViewModel::class.java
            ]

        detailsViewModel =
            ViewModelProvider(this)[
                ExpeditionDetailsViewModel::class.java
            ]

        savedViewModel =
            ViewModelProvider(this)[
                SavedViewModel::class.java
            ]

        planViewModel =
            ViewModelProvider(this)[
                PlanViewModel::class.java
            ]

        packingListViewModel =
            ViewModelProvider(this)[
                PackingListViewModel::class.java
            ]

        profileViewModel =
            ViewModelProvider(this)[
                ProfileViewModel::class.java
            ]

        registerViewModel =
            ViewModelProvider(this)[
                RegisterViewModel::class.java
            ]

        myExpeditionsViewModel =
            ViewModelProvider(this)[
                MyExpeditionsViewModel::class.java
            ]

        expeditionFormViewModel =
            ViewModelProvider(this)[
                ExpeditionFormViewModel::class.java
            ]

        setContent {

            HimalTheme {

                val authState by
                authViewModel.uiState.collectAsState()

                var showRegister by
                rememberSaveable {
                    mutableStateOf(false)
                }

                when {

                    authState.isCheckingSession -> {

                        Column(
                            modifier =
                                androidx.compose.ui.Modifier
                                    .fillMaxSize(),
                            verticalArrangement =
                                Arrangement.Center,
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    authState.isLoggedIn -> {

                        HimalNavGraph(
                            feedViewModel = feedViewModel,
                            detailsViewModel = detailsViewModel,
                            savedViewModel = savedViewModel,
                            planViewModel = planViewModel,
                            packingListViewModel =
                                packingListViewModel,
                            profileViewModel =
                                profileViewModel,
                            myExpeditionsViewModel =
                                myExpeditionsViewModel,
                            expeditionFormViewModel =
                                expeditionFormViewModel,

                            onLogout = {

                                feedViewModel.reset()
                                detailsViewModel.reset()
                                savedViewModel.reset()
                                planViewModel.reset()
                                packingListViewModel.reset()
                                profileViewModel.reset()
                                registerViewModel.reset()
                                myExpeditionsViewModel.reset()
                                expeditionFormViewModel.reset()

                                authViewModel.logout()
                            }
                        )
                    }

                    showRegister -> {

                        RegisterScreen(
                            viewModel = registerViewModel,

                            onBackToLogin = {

                                registerViewModel.reset()
                                showRegister = false
                            },

                            onRegistrationSuccess = { username ->

                                authViewModel.onUsernameChange(
                                    username
                                )

                                registerViewModel.reset()

                                showRegister = false
                            }
                        )
                    }

                    else -> {

                        LoginScreen(
                            viewModel = authViewModel,

                            onRegisterClick = {

                                showRegister = true
                            }
                        )
                    }
                }
            }
        }
    }
}