package com.tjliqy.twtstudio.homework2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_city_layout.*

import java.util.ArrayList
import java.util.HashMap
import android.widget.CompoundButton
import android.widget.TextView
import android.view.LayoutInflater
import android.R.attr.name
import com.tjliqy.twtstudio.homework2.CityActivity.MyAdapter




/**
 * Created by tjliqy on 2017/5/17.
 */

class CityActivity : AppCompatActivity() {
    private val cities = arrayOf("北京 朝阳", "江苏 宿迁", "江苏 南京", "江苏 徐州", "辽宁 朝阳")
    private val listems = ArrayList<Map<String, Any>>()
    private var myAdapter: MyAdapter? = null
    private val areaIds = arrayOf("1", "1", "1", "1", "1")
    private var dbHelper: DBHelper? = null
    private var name: Array<String?>? = null
    private var ids: Array<String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_layout)

        //初始化数据库
        dbHelper = DBHelper.instance
        changeAdapter(dbHelper!!.queryAllCities())


        city_listview.adapter = SimpleAdapter(application, listems,
                R.layout.activity_search_listview_item, arrayOf("name"),
                intArrayOf(R.id.result_text))
        city_listview.setOnItemClickListener { _, _, position, _ ->
            var bundle: Bundle? = intent.extras
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle.putString("city", name?.get(position))
            navigateBack(bundle)
        }
        add_city_button.setOnClickListener {
            navigate<SearchActivity>(null,0)
        }

//        myAdapter = MyAdapter(cities as Array<String?>, areaIds as Array<String?>, delete_city_button)
        city_listview.adapter = myAdapter

        delete_city_button.setOnClickListener({
            val result = myAdapter!!.getCheckedCities()
            for (i in result.indices) {
                dbHelper!!.deleteDataById(Integer.parseInt(result[i]))
            }
            changeAdapter(dbHelper!!.queryAllCities())
        })

    }

    private fun changeAdapter(list: List<Map<String, Any>>) {
        ids = arrayOfNulls<String>(list.size)
        name = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            ids!![i] = list[i]["id"].toString()
            name!![i] = list[i]["cityname"].toString()
        }
        myAdapter = MyAdapter(name!!, ids!!, delete_city_button)
        city_listview.adapter = myAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            var bundle:Bundle? = data.extras
            if (bundle == null){
                bundle = Bundle()
            }
            dbHelper!!.insertData(arrayOf("cityname"),
                    arrayOf(bundle.getString("city")))
            changeAdapter(dbHelper!!.queryAllCities())
            toast(bundle.getString("city"))
        }
    }


    inner class MyAdapter(cities: Array<String?>, cityIds: Array<String?>, button: Button) : BaseAdapter() {

        private inner class ViewSet {
            internal var textView: TextView? = null
            internal var checkBox: CheckBox? = null
        }

        private var cities: Array<String?>? = null
        private var checkedNum = 0
        private var button: Button? = null
        private var checkedArray: BooleanArray? = null
        private var cityIds: Array<String?>? = null

        init {
            this.cities = cities
            this.button = button
            this.cityIds = cityIds
            this.checkedArray = BooleanArray(cities.size)
            for (i in cities.indices) {
                this.checkedArray!![i] = false
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var viewSet: ViewSet? = null
            var view = convertView
            if (view == null) {
                viewSet = ViewSet()
                view = LayoutInflater.from(application).inflate(R.layout.activity_city_listview_item, null)
                viewSet.textView = view.findViewById(R.id.listview_item_textview) as TextView
                viewSet.checkBox = view.findViewById(R.id.listview_item_checkbox) as CheckBox
                view.tag = viewSet
            } else {
                viewSet = view.tag as ViewSet
            }
            viewSet.textView!!.text = cities!![position]
            viewSet.checkBox!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkedArray!![position] = true
                    checkedNum++
                    if (checkedNum === 1) {
                        button!!.isEnabled = true
                    }
                } else {
                    checkedArray!![position] = false
                    checkedNum--
                    if (checkedNum === 0) {
                        button!!.isEnabled = false
                    }
                }
            })
            return view!!

        }

        fun getCheckedCities(): Array<String> {
            val checkedCityIdList = ArrayList<String>()
            for (i in 0..checkedArray!!.size - 1) {
                if (checkedArray!![i] === true) {
                    checkedCityIdList.add(cityIds?.get(i) as String)
                }
            }
            val checkedCityIdArray = arrayOfNulls<String>(checkedCityIdList.size)
            return checkedCityIdList.toTypedArray()
        }


        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return cities!!.size
        }

    }
}
