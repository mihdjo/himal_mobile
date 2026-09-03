package com.himal.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector


enum class MainDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    FEED(
        route = "feed",
        label = "Feed",
        icon = Icons.Filled.Home
    ),

    SAVED(
        route = "saved",
        label = "Sačuvane",
        icon = Icons.Filled.Favorite
    ),

    PLAN(
        route = "plan",
        label = "Moj plan",
        icon = Icons.Filled.List
    ),

    PROFILE(
        route = "profile",
        label = "Profil",
        icon = Icons.Filled.Person
    )
}