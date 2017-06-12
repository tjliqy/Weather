package com.tjliqy.twtstudio.homework2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues


/**
 * Created by tjliqy on 2017/6/12.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, "test.db", null, 1) {

    private object Holder{
        val INSTANCE = DBHelper(ContextHolder.context!!)
    }

    companion object{
        val instance: DBHelper by lazy { Holder.INSTANCE }
    }

    //表名
    private val TableName = "cities"
    //数据库名
    private val DBName = "test.db"
    //数据库版本号
    private val DBVersion = 1
    private var context: Context? = context
    //数据库实例
    private var database: SQLiteDatabase? = null
    //此类自己的实例
    public var dbHelper: DBHelper? = null
    //创建数据库的语句
    private val createDBSql =
            "create table cities(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cityname TEXT NOT NULL);"

    public @Synchronized fun getInstance(context: Context): DBHelper {
        if (dbHelper == null) {
            dbHelper = DBHelper(context)
        }
        return dbHelper!!
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createDBSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    //插入数据，使用ContentValues方式传入
    fun insertData(keys: Array<String>, values: Array<String>) {
        val contentValues = ContentValues()
        for (i in keys.indices) {
            contentValues.put(keys[i], values[i])
        }
        database = writableDatabase
        database?.insert(TableName, null, contentValues)
    }

    //通过id删除数据
    fun deleteDataById(id: Int) {
        val args = arrayOf(id.toString())
        //这里需要可写的数据库
        database = writableDatabase
        database?.delete(TableName, "id=?", args)
    }

    //查询所有数据
    fun queryAllCities(): List<Map<String, Any>> {
        val list = ArrayList<Map<String, Any>>()
        //这里需要可读的数据库
        database = readableDatabase
        val cursor = database?.query(TableName, null, null, null, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val map = HashMap<String, Any>()
                map.put("id", cursor.getInt(cursor.getColumnIndex("id")))
                map.put("cityname", cursor.getString(cursor.getColumnIndex("cityname")))
                list.add(map)
            }
        }
        return list
    }


}
