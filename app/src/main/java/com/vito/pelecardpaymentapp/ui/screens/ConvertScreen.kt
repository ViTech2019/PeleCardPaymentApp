package com.vito.pelecardpaymentapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vito.domain.model.CurrencyRate
import com.vito.pelecardpaymentapp.ui.viewmodel.PaymentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertScreen(
    navController: NavController,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // loading state + list of currencies
    var isLoading by remember { mutableStateOf(true) }
    var currencyList by remember { mutableStateOf<List<CurrencyRate>>(emptyList()) }

    // fetch rates on first launch
    LaunchedEffect(Unit) {
        viewModel.getConvertedRates { rates ->
            currencyList = rates
                .filter { it.code != state.currency } // skip current currency
                .take(15) // display 15 currencies
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Currency Conversion") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) { // loading or list of currency
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ConversionList(
                    currencyList = currencyList,
                    baseCurrency = state.currency,
                    amount = state.amount
                )
            }
        }
    }
}

// list of currency conversions
@Composable
private fun ConversionList(
    currencyList: List<CurrencyRate>,
    baseCurrency: String,
    amount: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(currencyList) { item ->
            ConversionCard(item, baseCurrency, amount)
        }
    }
}

// card item to display currency conversion
@Composable
private fun ConversionCard(
    item: CurrencyRate,
    baseCurrency: String,
    amount: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Currency: ${item.code}")
            Text("Rate: 1 $baseCurrency = ${item.rate} ${item.code}")
            Text("Converted: $amount = ${item.convertedAmount} ${item.code}")
        }
    }
}
