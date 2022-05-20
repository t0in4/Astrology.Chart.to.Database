package com.eyehail.astrologytodatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.eyehail.astrologytodatabase.api.TheSignApiService
import com.eyehail.astrologytodatabase.model.DataBaseHelper
import com.eyehail.astrologytodatabase.model.HoroscopeData
import com.eyehail.astrologytodatabase.model.HoroscopeDatabaseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ohmanda.com/api/horoscope/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    private val theSignApiService by lazy {
        retrofit.create(TheSignApiService::class.java)
    }
    private val serverResponseView: TextView by lazy {
        findViewById(R.id.main_server_response)
    }
    lateinit var SIGN: String
    lateinit var DATE: String
    lateinit var HOROSCOPE: String

    // sqlite
    private val btn_add: Button by lazy {
        findViewById(R.id.btn_add)
    }
    private val btn_viewAll: Button by lazy {
        findViewById(R.id.btn_viewAll)
    }
    private val lv_horoscopeList: ListView by lazy {
        findViewById(R.id.lv_horoscopeList)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSignDailyHoroscope()
        btn_add.setOnClickListener {
            lateinit var horoscopeDatabase: HoroscopeDatabaseModel
            try {
                horoscopeDatabase = HoroscopeDatabaseModel(
                     -1,DATE, SIGN, HOROSCOPE
                )
                Toast.makeText(this, "$horoscopeDatabase", Toast.LENGTH_SHORT).show()
            } catch(e: Exception) {
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
                horoscopeDatabase = HoroscopeDatabaseModel(
                    -1, "error", "", ""
                )
            }
            val dataBaseHelper = DataBaseHelper(this)
            val success = dataBaseHelper.addOne(horoscopeDatabase)
            Toast.makeText(this, "success = $success", Toast.LENGTH_SHORT).show()

        }
        btn_viewAll.setOnClickListener {
            val dataBaseHelper = DataBaseHelper(this)
            val everyone = dataBaseHelper.getEveryOne()

            val horoscopeArrayAdapter = ArrayAdapter<HoroscopeDatabaseModel>(this, android.R.layout.simple_list_item_1, everyone)
            lv_horoscopeList.setAdapter(horoscopeArrayAdapter)

            //serverResponseView.text = ""
            //Toast.makeText(this, everyone.toString(), Toast.LENGTH_SHORT).show()

        }

        //delete item by click
        lv_horoscopeList.setOnItemClickListener{parent, view, position, id ->
            val clickedHoroscope: HoroscopeDatabaseModel = parent.getItemAtPosition(position) as HoroscopeDatabaseModel
            val dataBaseHelper = DataBaseHelper(this)
            dataBaseHelper.deleteOne(clickedHoroscope)
            Toast.makeText(this, "Deleted $clickedHoroscope", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSignDailyHoroscope() {
        val call = theSignApiService.returnDailyHoroscope()

        call.enqueue(object: Callback<HoroscopeData> {
            override fun onFailure(call: Call<HoroscopeData>, t: Throwable) {
                Log.e("Main Activity", "Failed to get results", t)
            }

            override fun onResponse(
                call: Call<HoroscopeData>,
                response: Response<HoroscopeData>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    val horoscope = result?.horoscope
                    val sign = result?.sign
                    val date = result?.date
                    DATE = result?.date.toString()
                    SIGN = result?.sign.toString()
                    HOROSCOPE = result?.horoscope.toString()
                    serverResponseView.text = HOROSCOPE

                    Log.d("response", "$horoscope")
                    Log.d("response","$sign $date")

                } else {
                    Log.e("Main Activity", "Failed to get search results \n" +
                            "${response.errorBody()?.string()?:""}")
                }
            }
        })
    }
}




