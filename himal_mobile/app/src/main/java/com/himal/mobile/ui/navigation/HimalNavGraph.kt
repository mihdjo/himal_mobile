package com.himal.mobile.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.himal.mobile.ui.details.ExpeditionDetailsScreen
import com.himal.mobile.ui.details.ExpeditionDetailsViewModel
import com.himal.mobile.ui.feed.FeedScreen
import com.himal.mobile.ui.feed.FeedViewModel
import com.himal.mobile.ui.plan.PlanScreen
import com.himal.mobile.ui.profile.ProfileScreen
import com.himal.mobile.ui.saved.SavedScreen
import com.himal.mobile.ui.saved.SavedViewModel
import com.himal.mobile.ui.plan.PlanViewModel
import com.himal.mobile.ui.packing.PackingListScreen
import com.himal.mobile.ui.packing.PackingListViewModel
import com.himal.mobile.ui.profile.ProfileViewModel

@Composable
fun HimalNavGraph(
    feedViewModel: FeedViewModel,
    detailsViewModel: ExpeditionDetailsViewModel,
    savedViewModel: SavedViewModel,
    planViewModel: PlanViewModel,
    packingListViewModel: PackingListViewModel,
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
){

    val navController = rememberNavController()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {

            HimalBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->

                    navController.navigate(route) {

                        popUpTo(
                            MainDestination.FEED.route
                        ) {
                            inclusive = false
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = MainDestination.FEED.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // FEED
            composable(
                route = MainDestination.FEED.route
            ) {

                FeedScreen(
                    viewModel = feedViewModel,
                    onLogout = onLogout,

                    onExpeditionClick = { expeditionId ->

                        navController.navigate(
                            "details/$expeditionId"
                        )
                    },

                    onMyExpeditionsClick = {

                        navController.navigate(
                            "my-expeditions"
                        )
                    }
                )
            }

            // SAVED
            composable(
                route = MainDestination.SAVED.route
            ) {

                SavedScreen(
                    viewModel = savedViewModel,

                    onExpeditionClick = { expeditionId ->

                        navController.navigate(
                            "details/$expeditionId"
                        )
                    },

                    onSessionExpired = {
                        onLogout()
                    }
                )
            }

            // MOJ PLAN
            composable(
                route = MainDestination.PLAN.route
            ) {

                PlanScreen(
                    viewModel = planViewModel,

                    onExpeditionClick = { expeditionId ->

                        navController.navigate(
                            "details/$expeditionId"
                        )
                    },

                    onPackingListClick = {

                        navController.navigate(
                            "packing-list"
                        )
                    },

                    onSessionExpired = {
                        onLogout()
                    }
                )
            }

            // PROFILE
            composable(
                route = MainDestination.PROFILE.route
            ) {

                ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = onLogout
                )
            }

            // MY EXPEDITIONS
            composable(
                route = "my-expeditions"
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Moje ekspedicije"
                    )
                }
            }

            // PACKING LIST
            composable(
                route = "packing-list"
            ) {

                PackingListScreen(
                    viewModel = packingListViewModel,

                    onSessionExpired = {
                        onLogout()
                    }
                )
            }

            // EXPEDITION DETAILS
            composable(
                route = "details/{expeditionId}",
                arguments = listOf(
                    navArgument("expeditionId") {
                        type = NavType.LongType
                    }
                )
            ) { detailsBackStackEntry ->

                val expeditionId =
                    detailsBackStackEntry.arguments
                        ?.getLong("expeditionId")
                        ?: return@composable

                ExpeditionDetailsScreen(
                    expeditionId = expeditionId,
                    viewModel = detailsViewModel,

                    onBack = {
                        navController.popBackStack()
                    },

                    onSessionExpired = {
                        onLogout()
                    }
                )
            }
        }
    }
}