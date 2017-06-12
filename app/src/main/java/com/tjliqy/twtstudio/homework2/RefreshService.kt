package com.tjliqy.twtstudio.homework2

import android.content.Intent
import android.os.IBinder
import android.R.string.cancel
import android.app.Service
import android.support.annotation.Nullable
import java.util.*


/**
 * Created by tjliqy on 2017/6/12.
 */
class RefreshService : Service() {
    internal var timer: Timer? = null
    internal var timerTask: TimerTask? = null

    override fun onCreate() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                val intent = Intent()
                intent.action = "com.weather.refresh"
                sendBroadcast(intent)
            }
        }
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        timer!!.schedule(timerTask, 0, 10000)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
        timer = null
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
