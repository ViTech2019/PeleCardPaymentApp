package com.vito.data.di

import com.vito.data.api.CurrencyApi
import com.vito.data.repository.ExchangeRateRepositoryImpl
import com.vito.domain.repository.ExchangeRateRepository
import com.vito.domain.usecase.GetExchangeRatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Retrofit instance with gson and base URL
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.freecurrencyapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // currencyApi from retrofit
    @Provides
    @Singleton
    fun provideCurrencyApi(retrofit: Retrofit): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

    //repository implementation for domain
    @Provides
    @Singleton
    fun provideExchangeRateRepository(
        api: CurrencyApi
    ): ExchangeRateRepository {
        return ExchangeRateRepositoryImpl(api)
    }

    // use case for getting exchange rates
    @Provides
    @Singleton
    fun provideGetExchangeRatesUseCase(
        repository: ExchangeRateRepository
    ): GetExchangeRatesUseCase {
        return GetExchangeRatesUseCase(repository)
    }
}
