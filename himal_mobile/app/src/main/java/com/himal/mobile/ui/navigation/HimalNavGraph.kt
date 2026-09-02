package com.himal.mobile.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.himal.mobile.ui.details.ExpeditionDetailsScreen
import com.himal.mobile.ui.details.ExpeditionDetailsViewModel
import com.himal.mobile.ui.equipmentmanagement.EquipmentManagementScreen
import com.himal.mobile.ui.equipmentmanagement.EquipmentManagementViewModel
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
import com.himal.mobile.ui.myexpeditions.MyExpeditionsScreen
import com.himal.mobile.ui.myexpeditions.MyExpeditionsViewModel
import com.himal.mobile.ui.expeditionform.ExpeditionFormScreen
import com.himal.mobile.ui.expeditionform.ExpeditionFormViewModel

@Composable
fun HimalNavGraph(
    feedViewModel: FeedViewModel,
    detailsViewModel: ExpeditionDetailsViewModel,
    savedViewModel: SavedViewModel,
    planViewModel: PlanViewModel,
    packingListViewModel: PackingListViewModel,
    profileViewModel: ProfileViewModel,
    myExpeditionsViewModel: MyExpeditionsViewModel,
    expeditionFormViewModel: ExpeditionFormViewModel,
    onLogout: () -> Unit,
    equipmentManagementViewModel: EquipmentManagementViewModel
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

                MyExpeditionsScreen(
                    viewModel = myExpeditionsViewModel,

                    onCreateClick = {
                        navController.navigate(
                            "create-expedition"
                        )
                    },

                    onOpenClick = { id ->
                        navController.navigate(
                            "details/$id"
                        )
                    },

                    onEditClick = { id ->
                        navController.navigate(
                            "edit-expedition/$id"
                        )
                    },

                    onEquipmentClick = { id ->
                        navController.navigate(
                            "manage-equipment/$id"
                        )
                    },

                    onSessionExpired = onLogout
                )
            }

            // CREATE EXPEDITION
            composable(
                route = "create-expedition"
            ) {

                ExpeditionFormScreen(
                    viewModel =
                        expeditionFormViewModel,

                    expeditionId = null,

                    onSaved = {

                        expeditionFormViewModel.reset()

                        navController.popBackStack()
                    },

                    onCancel = {

                        expeditionFormViewModel.reset()

                        navController.popBackStack()
                    },

                    onSessionExpired =
                        onLogout
                )
            }


            // EDIT EXPEDITION
            composable(
                route = "edit-expedition/{expeditionId}",
                arguments = listOf(
                    navArgument("expeditionId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val expeditionId =
                    backStackEntry.arguments
                        ?.getLong(
                            "expeditionId"
                        )
                        ?: return@composable

                ExpeditionFormScreen(
                    viewModel =
                        expeditionFormViewModel,

                    expeditionId =
                        expeditionId,

                    onSaved = {

                        expeditionFormViewModel.reset()

                        navController.popBackStack()
                    },

                    onCancel = {

                        expeditionFormViewModel.reset()

                        navController.popBackStack()
                    },

                    onSessionExpired =
                        onLogout
                )
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

            // EQUIPMENT
            composable(
                route = "manage-equipment/{expeditionId}",
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

                EquipmentManagementScreen(
                    expeditionId = expeditionId,
                    viewModel = equipmentManagementViewModel,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSessionExpired = onLogout
                )
            }
        }
    }
}