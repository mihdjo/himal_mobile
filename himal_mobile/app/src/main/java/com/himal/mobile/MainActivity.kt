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
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModelProvider
import com.himal.mobile.ui.auth.AuthViewModel
import com.himal.mobile.ui.auth.LoginScreen
import com.himal.mobile.ui.feed.FeedScreen
import com.himal.mobile.ui.feed.FeedViewModel
import com.himal.mobile.ui.saved.SavedViewModel
import com.himal.mobile.ui.plan.PlanViewModel
import com.himal.mobile.ui.theme.HimalTheme
import com.himal.mobile.ui.details.ExpeditionDetailsViewModel
import com.himal.mobile.ui.packing.PackingListViewModel
import com.himal.mobile.ui.navigation.HimalNavGraph

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var feedViewModel: FeedViewModel

    private lateinit var detailsViewModel: ExpeditionDetailsViewModel

    private lateinit var savedViewModel: SavedViewModel

    private lateinit var planViewModel: PlanViewModel

    private lateinit var packingListViewModel: PackingListViewModel

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

        setContent {

            HimalTheme {

                val authState by
                authViewModel.uiState.collectAsState()

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
                            onLogout = {

                                feedViewModel.reset()
                                detailsViewModel.reset()
                                savedViewModel.reset()
                                planViewModel.reset()
                                packingListViewModel.reset()

                                authViewModel.logout()
                            }
                        )
                    }

                    else -> {

                        LoginScreen(
                            viewModel = authViewModel
                        )
                    }
                }
            }
        }
    }
}