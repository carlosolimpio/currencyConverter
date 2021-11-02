package com.olimpio.currencyconvertion.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private lateinit var to: String
    private lateinit var from: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSource  = RemoteDataSource()

        loadCurrencies(dataSource)

        binding.buttonConvert.setOnClickListener {
            val value = binding.editTextValue.text.toString().toDouble()

            convert(from, to, value, dataSource)
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
                    initSpinners(currenciesList)
                }
            }

            override fun onFailure(call: Call<Map<String, Map<String, Currencies>>>, t: Throwable) {
                Log.d("olimpio", "onFailure: FAILURE")
            }
        })
    }

    private fun convert(from: String, to: String, value: Double, dataSource: RemoteDataSource) {
        dataSource.convert(from, to).enqueue(object: Callback<Map<String, Double>> {
            override fun onResponse(
                call: Call<Map<String, Double>>,
                response: Response<Map<String, Double>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { converted ->
                        convertedCurrency = converted.values.toDoubleArray()[0]
                        binding.textViewResult.text = String.format("%.2f", convertedCurrency * value)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String, Double>>, t: Throwable) {
                Log.d("olimpio", "onFailure: FAILURE")
            }
        })
    }

    private fun initSpinners(currencyList: List<String>) {
        if (currencyList.isNotEmpty()) {
            Log.d("olimpio", "initSpinners: NOT EMPTY")

            val spinnerFrom = binding.spinnerFrom
            val spinnerTo = binding.spinnerTo

            val adapterSpinnerFrom = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                currencyList
            )

            spinnerFrom.adapter = adapterSpinnerFrom
            spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.d("olimpio", "onItemSelected: ${currencyList[p2]}")
                    from = currencyList[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }
            }

            val adapterSpinnerTo = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                currencyList
            )

            spinnerTo.adapter = adapterSpinnerTo
            spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.d("olimpio", "onItemSelected: ${currencyList[p2]}")
                    to = currencyList[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }
            }
        } else {
            Log.d("olimpio", "initSpinners: currencyList is EMPTY")
        }
    }
}