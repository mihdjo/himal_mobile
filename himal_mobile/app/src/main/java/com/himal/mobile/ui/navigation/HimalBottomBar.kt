package com.himal.mobile.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HimalBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {

    NavigationBar {

        MainDestination.entries.forEach { destination ->

            NavigationBarItem(
                selected =
                    when {

                        currentRoute
                            ?.startsWith("details") == true -> {

                            destination == MainDestination.FEED
                        }

                        currentRoute == "my-expeditions" -> {

                            destination == MainDestination.FEED
                        }

                        currentRoute == "packing-list" -> {
                            destination == MainDestination.PLAN
                        }

                        else -> {

                            currentRoute == destination.route
                        }
                    },

                onClick = {
                    onNavigate(destination.route)
                },

                icon = {
                    Text(destination.icon)
                },

                label = {
                    Text(destination.label)
                }
            )
        }
    }
}