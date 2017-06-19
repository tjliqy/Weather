package com.tjliqy.twtstudio.homework2.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by tjliqy on 2017/6/19.
 */

@Database(entities = arrayOf(City::class),version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao():CityDao

}