package com.vito.pelecardpaymentapp.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vito.pelecardpaymentapp.ui.navigation.Screen
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: PaymentViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    // Handle back press to close app
    BackHandler { activity?.finish() }

    // live clock
    val time = remember { mutableStateOf(currentTime()) }
    LaunchedEffect(Unit) {
        while (true) {
            time.value = currentTime()
            kotlinx.coroutines.delay(1000)
        }
    }

    // top bar + settings button
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Current time
            Text(text = time.value, fontSize = 32.sp)

            Spacer(modifier = Modifier.height(32.dp))

            // enter amount
            AmountField(state.amount, viewModel::onAmountChange)

            // payments
            if (state.showPaymentOption) {
                PaymentRow(
                    isEnabled = state.isPaymentEnabled,
                    selected = state.numberOfPayments,
                    onToggle = viewModel::onPaymentEnabledChange,
                    onSelect = viewModel::onNumberOfPaymentsChange
                )
            }

            // currency
            if (state.showCurrencyOption) {
                CurrencySelector(
                    selectedCurrency = state.currency,
                    onCurrencyChange = viewModel::onCurrencyChange
                )
            }
            // signature
            if (state.showSignatureOption) {
                SignatureToggle(
                    checked = state.isSignatureEnabled,
                    onToggle = viewModel::onSignatureToggle
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // submit or exit app
            SubmitExitButtons(
                isSubmitEnabled = state.amount.trim().isNotEmpty(),
                goToSignature = state.isSignatureEnabled,
                navController = navController,
                activity = activity
            )
        }
    }
}

@Composable
private fun AmountField(value: String, onChange: (String) -> Unit) {
    Text("Amount:")
    TextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun PaymentRow(
    isEnabled: Boolean,
    selected: Int,
    onToggle: (Boolean) -> Unit,
    onSelect: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text("Payments:")
        Spacer(modifier = Modifier.width(8.dp))
        Switch(checked = isEnabled, onCheckedChange = onToggle)
        Spacer(modifier = Modifier.width(12.dp))

        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(
                onClick = { if (isEnabled) expanded = true },
                enabled = isEnabled
            ) {
                Text("$selected")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                (1..12).forEach {
                    DropdownMenuItem(
                        text = { Text("$it") },
                        onClick = {
                            onSelect(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun CurrencySelector(selectedCurrency: String, onCurrencyChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text("Currency:", modifier = Modifier.weight(1f))
        val currencies = listOf("USD", "ILS")
        currencies.forEach { currency ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(width = 64.dp, height = 36.dp)
                    .background(
                        if (selectedCurrency == currency) MaterialTheme.colorScheme.primary else Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable { onCurrencyChange(currency) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currency,
                    color = if (selectedCurrency == currency) Color.White else Color.Black
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun SignatureToggle(checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text("Signature:", modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle)
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun SubmitExitButtons(
    isSubmitEnabled: Boolean,
    goToSignature: Boolean,
    navController: NavController,
    activity: Activity?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                if (goToSignature) {
                    navController.navigate(Screen.Signature.route)
                } else {
                    navController.navigate(Screen.Receipt.route)
                }
            },
            enabled = isSubmitEnabled
        ) {
            Text("Submit")
        }

        OutlinedButton(onClick = { activity?.finish() }) {
            Text("Exit")
        }
    }
}
// returns current time as HOURS-MINUTES-SECONDS string
private fun currentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
