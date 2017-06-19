package com.tjliqy.twtstudio.homework2

import android.databinding.ObservableField
import android.databinding.ObservableInt

/**
 * Created by tjliqy on 2017/6/18.
 */
data class MainViewModel(var result: Weather.Result){

    var tempLow:ObservableField<String> = ObservableField(result.templow)
    var tempHigh:ObservableField<String> = ObservableField(result.temphigh)
    var city:ObservableField<String> = ObservableField(result.city)
    var cityId:ObservableField<String> = ObservableField(result.cityId)
    var date:ObservableField<String> = ObservableField(result.date)
    var week:ObservableField<String> = ObservableField(result.week)
    var weather:ObservableField<String> = ObservableField(result.weather)
    var temp:ObservableField<String> = ObservableField(result.temp)
    var infoLeft:ObservableField<String> = ObservableField("${result.city}\n\n${result.week}\n\n${result.weather}")
    var infoRight:ObservableField<String> = ObservableField("${result.date}\n\n${result.humidity}\n\n${result.pressure}")
    var weather1:ObservableField<String> = ObservableField(getDaliy(0,result))
    var weather2:ObservableField<String> = ObservableField(getDaliy(1,result))
    var weather3:ObservableField<String> = ObservableField(getDaliy(2,result))
    var weather4:ObservableField<String> = ObservableField(getDaliy(3,result))
    var date1:ObservableField<String> = ObservableField(getDate(0,result))
    var date2:ObservableField<String> = ObservableField(getDate(1,result))
    var date3:ObservableField<String> = ObservableField(getDate(2,result))
    var date4:ObservableField<String> = ObservableField(getDate(3,result))
    private fun getDaliy(index:Int,result: Weather.Result):String{
        if(result.daily.size > index){
            val daliy:Weather.Result.Daily = result.daily[index]
            return "${daliy.day.weather}\n${daliy.day.temphigh}/${daliy.night.templow}\n${daliy.week}"
        }
        return ""
    }

    private fun getDate(index:Int,result: Weather.Result):String{
        if(result.daily.size > index){
            return result.daily[index].date
        }
        return ""
    }

    fun changeWeather(temp:Weather.Result){
        tempLow.set(temp.templow)
        tempHigh.set(temp.temphigh)
        city.set(temp.city)
        cityId.set(temp.date)
        date.set(temp.date)
        week.set(temp.week)
        weather.set(temp.weather)
        this.temp.set(temp.temp)
        infoLeft.set("${temp.city}\n\n${temp.week}\n\n${temp.weather}")
        infoRight.set("${temp.date}\n\n${temp.humidity}\n\n${temp.pressure}")
        weather1.set(getDaliy(0,temp))
        weather2.set(getDaliy(1,temp))
        weather3.set(getDaliy(2,temp))
        weather4.set(getDaliy(3,temp))
        date1.set(getDate(0,temp))
        date2.set(getDate(1,temp))
        date3.set(getDate(2,temp))
        date4.set(getDate(3,temp))

    }
}