package com.himal.mobile.ui.navigation

enum class MainDestination(
    val route: String,
    val label: String,
    val icon: String
) {

    FEED(
        route = "feed",
        label = "Feed",
        icon = "🏔"
    ),

    SAVED(
        route = "saved",
        label = "Sačuvane",
        icon = "♥"
    ),

    PLAN(
        route = "plan",
        label = "Moj plan",
        icon = "🎒"
    ),

    PROFILE(
        route = "profile",
        label = "Profil",
        icon = "👤"
    )
}