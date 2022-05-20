package com.eyehail.astrologytodatabase.api

import com.eyehail.astrologytodatabase.model.HoroscopeData
import retrofit2.Call
import retrofit2.http.GET

interface TheSignApiService {
    @GET("sagittarius/")
    fun returnDailyHoroscope(): Call<HoroscopeData>
}