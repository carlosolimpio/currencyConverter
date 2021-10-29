package com.olimpio.currencyconvertion.network

import com.olimpio.currencyconvertion.network.model.Currencies
import retrofit2.Call

class RemoteDataSource {
    fun convert(from: String, to: String): Call<Map<String, Double>> {
        return RetrofitManager.getCurrencyConverterService()
            .getRates(parseApiInput(from, to))
    }

    fun getCurrencies(): Call<Map<String, Map<String, Currencies>>> {
        return RetrofitManager.getCurrenciesService().getCurrencies()
    }

    private fun parseApiInput(from: String, to: String) =
        from.toUpperCase() + "_" + to.toUpperCase()
}