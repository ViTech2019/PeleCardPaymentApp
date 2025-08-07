package com.vito.pelecardpaymentapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vito.pelecardpaymentapp.ui.screens.*
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentNavHost(navController: NavHostController = rememberNavController()) {
    // one viewmodel shared between all screens
    val sharedViewModel: PaymentViewModel = hiltViewModel()

    // nav host
    NavHost(navController = navController, startDestination = Screen.Main.route) {

        // main screen
        composable(Screen.Main.route) {
            MainScreen(navController, sharedViewModel)
        }

        // settings scree
        composable(Screen.Settings.route) {
            SettingsScreen(navController, sharedViewModel)
        }

        // signature screen
        composable(Screen.Signature.route) {
            SignatureScreen(navController, sharedViewModel)
        }

        // receipt screen
        composable(Screen.Receipt.route) {
            ReceiptScreen(navController, sharedViewModel)
        }

        // currency screen
        composable(Screen.Convert.route) {
            ConvertScreen(navController, sharedViewModel)
        }
    }
}
