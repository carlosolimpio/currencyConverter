package com.olimpio.currencyconvertion.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CURRENCY_CONVERTER_URL = "https://free.currconv.com/api/v7/"

object RetrofitManager {
    private fun getRetrofitInstance(urlPath: String) =
        Retrofit.Builder()
            .baseUrl(urlPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getCurrencyConverterService(): CurrencyService.Converter =
        getRetrofitInstance(CURRENCY_CONVERTER_URL).create(CurrencyService.Converter::class.java)

    fun getCurrenciesService(): CurrencyService.AllCurrencies =
        getRetrofitInstance(CURRENCY_CONVERTER_URL).create(CurrencyService.AllCurrencies::class.java)
}