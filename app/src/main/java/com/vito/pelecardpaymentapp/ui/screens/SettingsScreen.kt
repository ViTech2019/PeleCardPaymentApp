package com.vito.pelecardpaymentapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    // collect ui state
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SettingsTopBar { navController.popBackStack() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingSwitch(
                title = "Show Payment Option",
                checked = state.showPaymentOption,
                onToggle = viewModel::setShowPaymentOption
            )

            SettingSwitch(
                title = "Show Currency Option",
                checked = state.showCurrencyOption,
                onToggle = viewModel::setShowCurrencyOption
            )

            SettingSwitch(
                title = "Show Signature Option",
                checked = state.showSignatureOption,
                onToggle = viewModel::setShowSignatureOption
            )
        }
    }
}

// top bar + back button
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = { Text("Settings") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

// reusable toggle +text item
@Composable
private fun SettingSwitch(
    title: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}
