package com.himal.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.himal.mobile.ui.details.ExpeditionDetailsScreen
import com.himal.mobile.ui.details.ExpeditionDetailsViewModel
import com.himal.mobile.ui.feed.FeedScreen
import com.himal.mobile.ui.feed.FeedViewModel

@Composable
fun HimalNavGraph(
    feedViewModel: FeedViewModel,
    detailsViewModel: ExpeditionDetailsViewModel,
    onLogout: () -> Unit
) {

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "feed"
    ) {

        composable("feed") {

            FeedScreen(
                viewModel = feedViewModel,
                onLogout = onLogout,
                onExpeditionClick = { id ->

                    navController.navigate(
                        "details/$id"
                    )
                }
            )
        }

        composable(
            route = "details/{expeditionId}",
            arguments = listOf(
                navArgument("expeditionId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val expeditionId =
                backStackEntry.arguments
                    ?.getLong("expeditionId")
                    ?: return@composable

            ExpeditionDetailsScreen(
                expeditionId = expeditionId,
                viewModel = detailsViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onSessionExpired = onLogout
            )
        }
    }
}