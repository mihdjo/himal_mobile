package com.himal.mobile.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun HimalBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {

    NavigationBar(
        containerColor =
            MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {

        MainDestination.entries.forEach { destination ->

            val isSelected =
                when {

                    currentRoute
                        ?.startsWith("details") == true -> {

                        destination ==
                                MainDestination.FEED
                    }

                    currentRoute == "my-expeditions" ||
                            currentRoute == "create-expedition" ||
                            currentRoute
                                ?.startsWith(
                                    "edit-expedition"
                                ) == true ||
                            currentRoute
                                ?.startsWith(
                                    "manage-equipment"
                                ) == true -> {

                        destination ==
                                MainDestination.FEED
                    }

                    currentRoute ==
                            "packing-list" -> {

                        destination ==
                                MainDestination.PLAN
                    }

                    else -> {

                        currentRoute ==
                                destination.route
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

                    Icon(
                        imageVector =
                            destination.icon,
                        contentDescription =
                            destination.label
                    )
                },

                label = {

                    Text(
                        text =
                            destination.label
                    )
                },

                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor =
                            MaterialTheme
                                .colorScheme
                                .onPrimaryContainer,

                        selectedTextColor =
                            MaterialTheme
                                .colorScheme
                                .primary,

                        indicatorColor =
                            MaterialTheme
                                .colorScheme
                                .primaryContainer,

                        unselectedIconColor =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant,

                        unselectedTextColor =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )
            )
        }
    }
}