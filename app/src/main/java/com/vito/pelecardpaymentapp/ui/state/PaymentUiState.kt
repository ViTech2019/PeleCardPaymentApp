package com.vito.pelecardpaymentapp.ui.state

import android.graphics.Bitmap

//ui state for the payment flow
data class PaymentUiState(
    val amount: String = "",
    val isPaymentEnabled: Boolean = false, // are payments enabled
    val numberOfPayments: Int = 1, // amount of payments
    val currency: String = "ILS", // currency USD || ILS
    val isSignatureEnabled: Boolean = false, // is signature enabled
    val signatureCaptured: Boolean = false,
    val showPaymentOption: Boolean = true,
    val showCurrencyOption: Boolean = true,
    val showSignatureOption: Boolean = true,
    val signatureBitmap: Bitmap? = null // signature bitmap
)

