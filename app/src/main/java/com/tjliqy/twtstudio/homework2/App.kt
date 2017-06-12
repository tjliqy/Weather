package com.tjliqy.twtstudio.homework2

import android.app.Application

/**
 * Created by tjliqy on 2017/6/12.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHolder.initial(this)
    }
}