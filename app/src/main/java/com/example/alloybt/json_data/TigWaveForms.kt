package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class TigWaveForms(mode: String) {
    @Json(name = "0")
    TRIANGLE("Triangle"),
    @Json(name = "1")
    SINUS("SINUS"),
    @Json(name = "2")
    MEANDER("MEANDER"),
    @Json(name = "3")
    TRAPEZOID("TRAPEZOID")
}