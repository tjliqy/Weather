package com.tjliqy.twtstudio.homework2

import android.databinding.BaseObservable
import android.databinding.Bindable
import java.sql.ClientInfoStatus
import java.util.*

/**
 * Created by tjliqy on 2017/6/18.
 */
data class Weather(var status: String, var msg: String, @Bindable var result: Result) : BaseObservable() {
    data class Result(
            @Bindable var temphigh: String,
            @Bindable var templow: String,
            var temp: String,
            var city: String,
            var week: String,
            var weather: String):BaseObservable()
}