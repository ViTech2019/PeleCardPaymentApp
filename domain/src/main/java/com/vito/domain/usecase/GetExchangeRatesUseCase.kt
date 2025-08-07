package com.vito.domain.usecase

import com.vito.domain.model.CurrencyRate
import com.vito.domain.repository.ExchangeRateRepository

// fetch exchange rates based on base currency and amount
class GetExchangeRatesUseCase(
    private val repository: ExchangeRateRepository
) {
    suspend operator fun invoke(
        baseCurrency: String,
        amount: Double
    ): List<CurrencyRate> {
        return repository.getExchangeRates(baseCurrency).map { (code, rate) ->
            CurrencyRate(
                code = code,
                rate = rate,
                convertedAmount = (amount * rate * 100).toInt() / 100.0 // rounding to 2 decimal
            )
        }
    }
}
