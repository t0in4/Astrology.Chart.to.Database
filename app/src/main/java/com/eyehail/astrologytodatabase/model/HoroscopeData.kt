package com.eyehail.astrologytodatabase.model

import com.squareup.moshi.Json

data class HoroscopeData(
    @field: Json(name="sign") val sign: String,
    @field: Json(name="date") val date: String,
    @field: Json(name="horoscope") val horoscope: String
) {
    override fun toString(): String {
        return "$sign $date $horoscope"
    }
}
