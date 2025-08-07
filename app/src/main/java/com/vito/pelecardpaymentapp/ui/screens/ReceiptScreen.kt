package com.vito.pelecardpaymentapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vito.pelecardpaymentapp.ui.navigation.Screen
import com.vito.pelecardpaymentapp.ui.state.PaymentUiState
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    // collect current ui state
    val state by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        // main layout column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // title
            ReceiptHeader()
            //receipt details
            ReceiptDetails(state)
            // signature
            ReceiptSignature(state)
            // buttons
            ReceiptActions(
                onConvert = { navController.navigate(Screen.Convert.route) },
                onFinish = {
                    viewModel.clearForm()
                    navController.popBackStack(Screen.Main.route, inclusive = false)
                }
            )
        }
    }
}

@Composable
private fun ReceiptHeader() {
    Text(
        text = "Receipt",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun ReceiptDetails(state: PaymentUiState) {
    // amount
    Text("Amount:  ${state.amount}", fontSize = 20.sp, fontWeight = FontWeight.Medium)

    // payment info
    if (state.showPaymentOption && state.isPaymentEnabled) {
        Text("Payments:  ${state.numberOfPayments}", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }

    // elected currency
    if (state.showCurrencyOption) {
        Text("Currency:  ${state.currency}", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ReceiptSignature(state: PaymentUiState) {
    // display signature image if user signed
    if (state.showSignatureOption && state.isSignatureEnabled && state.signatureCaptured) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Signature:", fontSize = 20.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))

        state.signatureBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .border(1.dp, Color.Gray)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Signature",
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.9f)
                )
            }
        }
    }
}

@Composable
private fun ReceiptActions(
    onConvert: () -> Unit,
    onFinish: () -> Unit
) {
    Spacer(modifier = Modifier.height(32.dp))

    // convert btn
    Button(
        onClick = onConvert,
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(48.dp)
    ) {
        Text("Convert", fontSize = 18.sp)
    }

    Spacer(modifier = Modifier.height(16.dp))

    // finish btn
    OutlinedButton(
        onClick = onFinish,
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(48.dp)
    ) {
        Text("Finish", fontSize = 18.sp)
    }
}
