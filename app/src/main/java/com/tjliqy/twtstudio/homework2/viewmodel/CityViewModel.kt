package com.tjliqy.twtstudio.homework2.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.MutableBoolean
import com.tjliqy.twtstudio.homework2.dao.City
import com.tjliqy.twtstudio.homework2.dao.DatabaseCreator

/**
 * Created by tjliqy on 2017/6/19.
 */
class CityViewModel(application: Application) : AndroidViewModel(application) {
    private var ABSENT = MutableLiveData<List<City>>()
    private val mObservableCities: LiveData<List<City>>?

    init {
        ABSENT.value = null
        val databaseCreator = DatabaseCreator.instance

        val databaseCreated = databaseCreator.isDatabaseCreated()
        mObservableCities = Transformations.switchMap<Boolean, List<City>>(databaseCreated
        ) { isDbCreated ->
            if (java.lang.Boolean.TRUE != isDbCreated) { // Not needed here, but watch out for null
                ABSENT
            } else {

                databaseCreator.getDatabase()?.cityDao()?.getAll()
            }
        }

        databaseCreator.createDb(this.getApplication<Application>())
    }

    fun getProducts(): LiveData<List<City>>? {
        return mObservableCities
    }
}