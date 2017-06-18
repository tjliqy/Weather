package com.tjliqy.twtstudio.homework2

import android.app.Application
import com.ohmerhe.kolley.request.Http

/**
 * Created by tjliqy on 2017/6/12.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHolder.initial(this)
        Http.init(this)

    }
}