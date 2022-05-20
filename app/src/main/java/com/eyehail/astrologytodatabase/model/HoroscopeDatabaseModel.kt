package com.eyehail.astrologytodatabase.model

data class HoroscopeDatabaseModel(
    val id: Int,
    val date: String,
    val sign: String,
    val horoscope: String
) {
    override fun toString(): String {
        return "$id $date $sign $horoscope"
    }
}
