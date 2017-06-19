package com.tjliqy.twtstudio.homework2.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by tjliqy on 2017/6/19.
 */
class DatabaseCreator{
    var mIsDatabaseCreated: MutableLiveData<Boolean> = MutableLiveData()

    var mDb:AppDatabase? = null
    val DATABASE_NAME = "cities"

    fun getDatabase(): AppDatabase? {
        return mDb
    }

      private object Holder{
        val INSTANCE = DatabaseCreator()
    }

    companion object{
        val instance: DatabaseCreator by lazy { Holder.INSTANCE }
    }


    private val mInitializing = AtomicBoolean(true)

    fun isDatabaseCreated(): LiveData<Boolean> {
        return mIsDatabaseCreated
    }


    /**
     * Creates or returns a previously-created database.
     *
     *
     * Although this uses an AsyncTask which currently uses a serial executor, it's thread-safe.
     */
    fun createDb(context: Context) {

        Log.d("DatabaseCreator", "Creating DB from " + Thread.currentThread().name)

        if (!mInitializing.compareAndSet(true, false)) {
            return  // Already initializing
        }

        mIsDatabaseCreated.setValue(false)// Trigger an update to show a loading screen.
        object : AsyncTask<Context, Void, Void>() {

            override fun doInBackground(vararg params: Context): Void? {
                Log.d("DatabaseCreator",
                        "Starting bg job " + Thread.currentThread().name)

                val context = params[0].applicationContext

                // Reset the database to have new data on every run.
                context.deleteDatabase(DATABASE_NAME)

                // Build the database!
                val db = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DATABASE_NAME).build()

                Log.d("DatabaseCreator",
                        "DB was populated in thread " + Thread.currentThread().name)

                mDb = db
                return null
            }

            override fun onPostExecute(ignored: Void) {
                // Now on the main thread, notify observers that the db is created and ready.
                mIsDatabaseCreated.setValue(true)
            }
        }.execute(context.applicationContext)
    }
}