package com.vito.domain.model

//currency conversion result
data class CurrencyRate(
    val code: String,
    val rate: Double,
    val convertedAmount: Double
)
