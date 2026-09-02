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

            val isSelected =
                when {

                    // Expedition details pripada Feed sekciji
                    currentRoute
                        ?.startsWith("details") == true -> {

                        destination == MainDestination.FEED
                    }

                    // Moje ekspedicije + Create + Edit
                    // takođe pripadaju Feed sekciji
                    currentRoute == "my-expeditions" ||
                            currentRoute == "create-expedition" ||
                            currentRoute
                                ?.startsWith("edit-expedition") == true -> {

                        destination == MainDestination.FEED
                    }

                    // Packing lista pripada Moj Plan sekciji
                    currentRoute == "packing-list" -> {

                        destination == MainDestination.PLAN
                    }

                    // Glavne navbar destinacije
                    else -> {

                        currentRoute == destination.route
                    }
                }

            NavigationBarItem(
                selected = isSelected,

                onClick = {
                    onNavigate(
                        destination.route
                    )
                },

                icon = {
                    Text(
                        destination.icon
                    )
                },

                label = {
                    Text(
                        destination.label
                    )
                }
            )
        }
    }
}