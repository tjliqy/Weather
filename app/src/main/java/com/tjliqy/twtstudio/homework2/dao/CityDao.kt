package com.tjliqy.twtstudio.homework2.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * Created by tjliqy on 2017/6/19.
 */

@Dao
interface CityDao {
    @Query("SELECT * FROM city")
    fun getAll():LiveData<List<City>>
}