package com.eyehail.astrologytodatabase.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(
    context: Context?

) : SQLiteOpenHelper(context, "horoscope.db", null, 1) {
    val HOROSCOPE_TABLE = "HOROSCOPE_TABLE"
    val COLUMN_ID = "ID"
    val COLUMN_SIGN = "SIGN"
    val COLUMN_DATE = "DATE"
    val COLUMN_HOROSCOPE = "HOROSCOPE"
    override fun onCreate(db: SQLiteDatabase?) {

        val createTableStatement = "CREATE TABLE $HOROSCOPE_TABLE ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_DATE TEXT," +
                "$COLUMN_SIGN TEXT, $COLUMN_HOROSCOPE TEXT)"
        db?.execSQL(createTableStatement)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addOne(horoscopeDatabase: HoroscopeDatabaseModel): Boolean {
        val db: SQLiteDatabase = this.getWritableDatabase()
        val cv: ContentValues = ContentValues()
        cv.put(COLUMN_DATE, horoscopeDatabase.date)
        cv.put(COLUMN_SIGN, horoscopeDatabase.sign)
        cv.put(COLUMN_HOROSCOPE, horoscopeDatabase.horoscope)
        val insert = db.insert(HOROSCOPE_TABLE, null, cv)
        return insert != -1L
    }

    fun getEveryOne(): List<HoroscopeDatabaseModel> {
        var returnList: MutableList<HoroscopeDatabaseModel> = mutableListOf()
        var queryString = "SELECT * FROM $HOROSCOPE_TABLE"
        val db: SQLiteDatabase = this.getReadableDatabase()
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToFirst()) {
            do {
                val columnId = cursor.getInt(0)
                val columnDate = cursor.getString(1)
                val columnSign = cursor.getString(2)
                val columnHoroscope = cursor.getString(3)
                val newHoroscope = HoroscopeDatabaseModel(
                    columnId, columnDate, columnSign, columnHoroscope
                )
                returnList.add(newHoroscope)
            } while (cursor.moveToNext())


        }
        cursor.close()
        db.close()
        return returnList
    }

    fun deleteOne(horoscopeDatabase: HoroscopeDatabaseModel): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val queryString = "DELETE FROM $HOROSCOPE_TABLE WHERE $COLUMN_ID = ${horoscopeDatabase.id}"
        val cursor = db.rawQuery(queryString, null)
        return cursor.moveToFirst()
    }

}