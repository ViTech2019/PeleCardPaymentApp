package com.vito.domain.repository

interface ExchangeRateRepository {
    suspend fun getExchangeRates(baseCurrency: String): Map<String, Double>
}
