package com.vito.pelecardpaymentapp.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vito.domain.model.CurrencyRate
import com.vito.domain.usecase.GetExchangeRatesUseCase
import com.vito.pelecardpaymentapp.ui.state.PaymentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {

    // app ui state shared between all screens
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState

    //----------main screen----------//
    // amount
    fun onAmountChange(value: String) {
        _uiState.value = _uiState.value.copy(amount = value)
    }

    // toggle payment
    fun onPaymentEnabledChange(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isPaymentEnabled = enabled)
    }

    // picked number of payments
    fun onNumberOfPaymentsChange(count: Int) {
        _uiState.value = _uiState.value.copy(numberOfPayments = count)
    }

    // change currency
    fun onCurrencyChange(value: String) {
        _uiState.value = _uiState.value.copy(currency = value)
    }

    //----------signature screen----------//
    // toggle signature
    fun onSignatureToggle(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isSignatureEnabled = enabled)
    }

    // user submitted signature
    fun onSignatureSubmitted() {
        _uiState.value = _uiState.value.copy(signatureCaptured = true)
    }

    // clear signature
    fun clearSignature() {
        _uiState.value = _uiState.value.copy(signatureCaptured = false)
    }

    // store signature bitmap in state
    fun saveSignature(bitmap: Bitmap) {
        _uiState.value = _uiState.value.copy(
            signatureBitmap = bitmap,
            signatureCaptured = true
        )
    }

    //----------settings screen----------//
    // show/hide payment options
    fun setShowPaymentOption(value: Boolean) {
        _uiState.value = _uiState.value.copy(showPaymentOption = value)
    }

    // show/hide currency
    fun setShowCurrencyOption(value: Boolean) {
        _uiState.value = _uiState.value.copy(showCurrencyOption = value)
    }

    // show/hide signature section
    fun setShowSignatureOption(value: Boolean) {
        _uiState.value = _uiState.value.copy(showSignatureOption = value)
    }

    //----------convert screen----------//
    // fetch converted rates based on currency + amount
    fun getConvertedRates(onResult: (List<CurrencyRate>) -> Unit) {
        viewModelScope.launch {
            val amount = _uiState.value.amount.toDoubleOrNull() ?: 0.0
            val result = getExchangeRatesUseCase(_uiState.value.currency, amount)
            onResult(result)
        }
    }

    // reset all state
    fun clearForm() {
        _uiState.value = PaymentUiState()
    }
}
