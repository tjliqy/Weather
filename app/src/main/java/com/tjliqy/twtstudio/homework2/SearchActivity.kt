package com.tjliqy.twtstudio.homework2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleAdapter
import kotlinx.android.synthetic.main.activity_search_layout.*

import java.util.ArrayList
import java.util.HashMap
import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray


/**
 * Created by tjliqy on 2017/5/17.
 */

class SearchActivity : AppCompatActivity() {
    private val cities = arrayOf("北京 朝阳", "江苏 宿迁", "江苏 南京", "江苏 徐州", "辽宁 朝阳")
    private var listems = ArrayList<Map<String, String>>()
    private val url = "http://api.jisuapi.com/weather/city?appkey=8de28bcece446885"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_layout)


        search_button.setOnClickListener {
            val input_text = search_text.text.toString()
            Getcity().execute(url, input_text)
        }

        result_list.setOnItemClickListener { _, _, position, _ ->
            var bundle:Bundle? = intent.extras
            if(bundle == null){
                bundle = Bundle()
            }
            bundle.putString("city", listems[position]["name"].toString())
            navigateBack(bundle)
        }
    }

    private inner class Getcity : AsyncTask<String, String, String>() {
        private var input_text = ""

        override fun doInBackground(vararg params: String): String {
            input_text = params[1]
            return openConnect(params[0])
        }

        override fun onPostExecute(result: String) {
            try {
                listems.clear()
                listems = parseCities(result, input_text)
                result_list.adapter = SimpleAdapter(application, listems,
                        R.layout.activity_search_listview_item, arrayOf("name"),
                        intArrayOf(R.id.result_text))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun openConnect(address: String): String {
        var result = ""
        try {
            val url = URL(address)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val `in` = connection.getInputStream()
            val reader = BufferedReader(InputStreamReader(`in`, "UTF-8"))
            var line:String? = null
            while ({line = reader.readLine(); line}() != null) {
                result += line
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    fun parseCities(json: String, input_text: String): ArrayList<Map<String, String>> {
        var map: MutableMap<String, String>
        try {
            val obj1 = JSONObject(json)
            val array = obj1.getJSONArray("result")
            var cityName = input_text
            for (i in 0..array.length() - 1) {
                val obj2 = array.getJSONObject(i)
                var parentid = obj2.getString("parentid")
                if (obj2.getString("city").trim { it <= ' ' }.contains(cityName)) {
                    cityName = obj2.getString("city").trim { it <= ' ' }
                    map = HashMap<String, String>()
                    if (parentid == "0") {
                        map.put("name", cityName)
                        listems.add(map)
                        break
                    } else {
                        for (j in 0..array.length() - 1) {
                            val obj3 = array.getJSONObject(j)
                            if (obj3.getString("cityid") == parentid) {
                                parentid = obj3.getString("parentid")
                                cityName = obj3.getString("city") + " " + cityName
                                if (parentid == "0") {
                                    map.put("name", cityName)
                                    listems.add(map)
                                    break
                                } else {
                                    for (k in 0..array.length() - 1) {
                                        val obj4 = array.getJSONObject(k)
                                        if (obj4.getString("cityid") == parentid) {
                                            cityName = obj4.getString("city") + " " + cityName
                                            map.put("name", cityName)
                                            listems.add(map)
                                            break
                                        } else {
                                            continue
                                        }
                                    }
                                }
                            } else {
                                continue
                            }
                        }
                    }
                } else {
                    continue
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return listems
    }


}
