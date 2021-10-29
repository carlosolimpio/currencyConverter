package com.olimpio.currencyconvertion.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.olimpio.currencyconvertion.databinding.ActivityMainBinding
import com.olimpio.currencyconvertion.network.RemoteDataSource
import com.olimpio.currencyconvertion.network.model.Currencies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val currenciesList = arrayListOf<String>()
    private var convertedCurrency: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSource  = RemoteDataSource()

        loadCurrencies(dataSource)
        initSpinners(currenciesList)


        binding.buttonConvert.setOnClickListener {
            val value = binding.editTextTextPersonName.text.toString().toDouble()

            val from = binding.spinnerFrom.selectedItem.toString() // NPE
            val to = binding.spinnerTo.selectedItem.toString()

            convert(from, to, dataSource)

            binding.textViewResult.text = (convertedCurrency * value).toString()
        }
    }

    private fun loadCurrencies(dataSource: RemoteDataSource) {
        dataSource.getCurrencies().enqueue(object: Callback<Map<String, Map<String, Currencies>>> {
            override fun onResponse(
                call: Call<Map<String, Map<String, Currencies>>>,
                response: Response<Map<String, Map<String, Currencies>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { currencies ->
                        currencies.values.forEach {
                            it.forEach { actual -> currenciesList.add(actual.value.id) }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Map<String, Currencies>>>, t: Throwable) {
                Log.d("olimpio", "onFailure: FAILURE")
            }
        })
    }

    private fun convert(from: String, to: String, dataSource: RemoteDataSource) {
        dataSource.convert(from, to).enqueue(object: Callback<Map<String, Double>> {
            override fun onResponse(
                call: Call<Map<String, Double>>,
                response: Response<Map<String, Double>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { converted ->
                        convertedCurrency = converted.values.toDoubleArray()[0]
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Double>>, t: Throwable) {
                Log.d("olimpio", "onFailure: FAILURE")
            }
        })
    }

    private fun initSpinners(currencyList: List<String>) {
        val adapterSpinnerFrom = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencyList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFrom.adapter = adapter
        }

        val adapterSpinnerTo = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencyList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTo.adapter = adapter
        }
    }
}