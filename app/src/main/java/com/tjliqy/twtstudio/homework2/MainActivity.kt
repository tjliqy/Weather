package com.tjliqy.twtstudio.homework2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import org.json.JSONArray
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import com.google.gson.GsonBuilder
import com.ohmerhe.kolley.request.Http
import com.tjliqy.twtstudio.homework2.databinding.ActivityMainBinding
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    var weather: Weather = Weather()
    var viewModel:MainViewModel = MainViewModel(weather.result)
    lateinit var binding:ActivityMainBinding
    private val baseUrl = "http://api.jisuapi.com/weather/query?appkey=8de28bcece446885&city="
    private var defaultName = "天津"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.data = viewModel


        city_button.setOnClickListener {
            navigate<CityActivity>(null, 1)
        }

        getWeather(defaultName)
        //启动广播接收器
        val inf = IntentFilter()
        inf.addAction("com.weather.refresh")
        registerReceiver(broadcastReceiver, inf)

        startService(Intent(this, RefreshService::class.java))


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val bundle = data?.extras
            val cityName = bundle?.getString("city")
            val params = cityName?.split(" ")
            if (params != null) {
                defaultName = params[params.size - 1]

            }
            toast(bundle?.getString("city"))
            getWeather(defaultName)

        }
    }

    private fun getWeather(cityId: String){
        Http.get {
            url = baseUrl + cityId
            tag = this@MainActivity
            onSuccess { bytes ->
                val json = bytes.toString(Charset.defaultCharset())
                viewModel.changeWeather(
                        GsonBuilder().create()
                        .fromJson(json, Weather::class.java).result)
            }
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == "com.weather.refresh") {
                getWeather(defaultName)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, RefreshService::class.java))
        unregisterReceiver(broadcastReceiver)

    }

}
