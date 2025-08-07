package com.vito.data.repository

import com.vito.data.Constants.FREE_CURRENCY_API_KEY
import com.vito.data.api.CurrencyApi
import com.vito.domain.repository.ExchangeRateRepository

class ExchangeRateRepositoryImpl(
    private val api: CurrencyApi
) : ExchangeRateRepository {
    //fetch exchange rates from the remote API based on baseCurrency
    override suspend fun getExchangeRates(baseCurrency: String): Map<String, Double> {
        val response = api.getExchangeRates(
            apiKey = FREE_CURRENCY_API_KEY,
            baseCurrency = baseCurrency
        )
        return response.data
    }
}