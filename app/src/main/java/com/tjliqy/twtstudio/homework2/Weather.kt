package com.tjliqy.twtstudio.homework2

import java.util.*

/**
 * Created by tjliqy on 2017/6/18.
 */
data class Weather(var status: String = "", var msg: String = "", var result: Result = Weather.Result()) {
    data class Result(
            var temphigh: String = "",
            var templow: String = "",
            var temp: String = "",
            var city: String = "",
            var cityId: String = "",
            var date: String = "",
            var week: String = "",
            var weather: String = "",
            var humidity: String = "",
            var pressure: String = "",
            var daily: LinkedList<Daily> = LinkedList<Result.Daily>()) {
        data class Daily(
                var week: String = "",
                var day: DayOrNight = Daily.DayOrNight(),
                var night: DayOrNight = Daily.DayOrNight(),
                var date: String = ""
        ) {
            data class DayOrNight(
                    var weather: String = "",
                    var temphigh: String = "",
                    var templow: String = ""
            )
        }
    }
}