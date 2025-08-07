package com.vito.pelecardpaymentapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.vito.pelecardpaymentapp.ui.navigation.PaymentNavHost
import com.vito.pelecardpaymentapp.ui.theme.PeleCardPaymentAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PeleCardPaymentAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PaymentNavHost()
                }
            }
        }
    }
}
