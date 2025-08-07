package com.vito.data.api

import com.vito.data.remote.CurrencyResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    // get the latest exchange rates
    @GET("v1/latest")
    suspend fun getExchangeRates(
        @Query("apikey") apiKey: String, //API key for auth
        @Query("base_currency") baseCurrency: String
    ): CurrencyResponseDto
}
