package com.vito.data.repository

import com.vito.data.api.CurrencyApi
import com.vito.domain.repository.ExchangeRateRepository

class ExchangeRateRepositoryImpl(
    private val api: CurrencyApi
) : ExchangeRateRepository {
    //fetch exchange rates from the remote API based on baseCurrency
    override suspend fun getExchangeRates(baseCurrency: String): Map<String, Double> {
        val response = api.getExchangeRates(
            apiKey = "fca_live_1ErCJ6u7aK119uHOsDegz53YQlSp6wthWJGR4vwH",
            baseCurrency = baseCurrency
        )
        return response.data
    }
}
