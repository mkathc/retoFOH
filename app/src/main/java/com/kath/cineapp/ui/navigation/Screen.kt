package com.kath.cineapp.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_screen")
    data object Login : Screen(route = "login_screen")
    data object CandyStore : Screen(route = "candy_store_screen")
    data object Payment : Screen(route = "payment_screen")

}
