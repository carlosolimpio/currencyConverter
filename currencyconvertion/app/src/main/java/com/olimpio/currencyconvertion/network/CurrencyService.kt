package com.olimpio.currencyconvertion.network

import com.olimpio.currencyconvertion.network.Constants.API_KEY
import com.olimpio.currencyconvertion.network.model.Currencies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    interface Converter {
        @GET("convert")
        fun getRates(
            @Query("q") query: String,
            @Query("compact") size: String = "ultra",
            @Query("apiKey") key: String = API_KEY
        ): Call<Map<String, Double>>
    }

    interface AllCurrencies {
        @GET("currencies")
        fun getCurrencies(@Query("apiKey") key: String = API_KEY): Call<Map<String, Map<String, Currencies>>>
    }
}