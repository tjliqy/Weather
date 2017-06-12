package com.tjliqy.twtstudio.homework2

import android.content.Context

/**
 * Created by tjliqy on 2017/6/12.
 */
object ContextHolder {

    var context: Context? = null
        internal set

    fun initial(context: Context) {
        this.context = context
    }
}