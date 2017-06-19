package com.tjliqy.twtstudio.homework2.dao

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by tjliqy on 2017/6/19.
 */

@Entity
class City {

    @PrimaryKey internal var id: Int = 0

    @ColumnInfo(name = "cityname")
    internal var cityName: String? = null
}
