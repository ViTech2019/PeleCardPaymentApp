package com.vito.pelecardpaymentapp.ui.navigation

// screens with route names
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Settings : Screen("settings")
    object Signature : Screen("signature")
    object Receipt : Screen("receipt")
    object Convert : Screen("convert")
}
