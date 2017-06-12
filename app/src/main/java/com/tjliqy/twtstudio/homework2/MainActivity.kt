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


class MainActivity : AppCompatActivity() {
    private val url = "http://api.jisuapi.com/weather/query?appkey=8de28bcece446885&city="
    private var defaultName = "天津"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        city_button.setOnClickListener {
            navigate<CityActivity>(null,1)
        }
        val getWeather = Getweather()
        getWeather.execute(url, defaultName)

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
            val getWeather = Getweather()
            getWeather.execute(url, defaultName)
        }
    }

    private inner class Getweather : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String): String {
            return openConnection(p0[0], p0[1])
        }

        override fun onPostExecute(result: String) {
            try {
                val `object` = JSONObject(result)
                val today = `object`.get("result") as JSONObject
                //获取当天天气
                highest_text.text = today.getString("temphigh")
                lowest_text.text = today.getString("templow")
                now_text.text = (today.getString("temp") + "\u2103")
                info_left_text.text = (today.getString("city") + "\n\n" + today.getString("week") + "\n\n" + today.getString("weather"))
                info_right.text = (today.getString("date") + "\n\n" + today.getString("humidity") + "\n\n" + today.getString("pressure"))
                Log.i("infotest", today.getString("date") + "\n\n" + today.getString("winddirect") + "\n\n" + today.getString("windpower"))
                Log.i("infotest", today.getString("city") + "\n\n" + today.getString("week") + "\n\n" + today.getString("weather"))
                //获取后四天的预告
                val forecast = today.get("daily") as JSONArray
                first_weather.text = ((forecast.get(0) as JSONObject).getJSONObject("day").getString("weather") + "\n\n" +
                        ((forecast.get(0) as JSONObject).getJSONObject("day").getString("temphigh") + "/" + (forecast.get(0) as JSONObject).getJSONObject("night").getString("templow")))

                second_weather.text = ((forecast.get(1) as JSONObject).getJSONObject("day").getString("weather") + "\n\n" +
                        ((forecast.get(1) as JSONObject).getJSONObject("day").getString("temphigh") + "/" + (forecast.get(1) as JSONObject).getJSONObject("night").getString("templow")))

                third_weather.text = ((forecast.get(2) as JSONObject).getJSONObject("day").getString("weather") + "\n\n" +
                        ((forecast.get(2) as JSONObject).getJSONObject("day").getString("temphigh") + "/" + (forecast.get(2) as JSONObject).getJSONObject("night").getString("templow")))

                fourth_weather.text = ((forecast.get(3) as JSONObject).getJSONObject("day").getString("weather") + "\n\n" +
                        ((forecast.get(3) as JSONObject).getJSONObject("day").getString("temphigh") + "/" + (forecast.get(3) as JSONObject).getJSONObject("night").getString("templow")))

                first_date.text = (forecast.get(1) as JSONObject).getString("date")
                second_date.text = (forecast.get(2) as JSONObject).getString("date")
                third_date.text = (forecast.get(3) as JSONObject).getString("date")
                fourth_date.text = (forecast.get(4) as JSONObject).getString("date")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun openConnection(address: String, cityId: String): String {
            var result = ""
            try {
                val url = URL(address + cityId)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
                var line: String? = null
                while ({ line = reader.readLine(); line }() != null) {
                    result += line
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return result
        }

    }


    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == "com.weather.refresh") {
                //Toast.makeText(application, "refresh", Toast.LENGTH_LONG).show()
                val getweather = Getweather()
                getweather.execute(url, defaultName)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, RefreshService::class.java))
        unregisterReceiver(broadcastReceiver)

    }

}
