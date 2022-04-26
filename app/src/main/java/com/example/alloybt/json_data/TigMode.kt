package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class TigMode(weldMode: String) {
    @Json()  //(name = "0")
    AC("AC"),
    @Json(name = "1")
    AC_PULSE("AC Pulse"),
    @Json(name = "2")
    DC("DC"),
    @Json(name = "3")
    DC_PULSE("DC Pulse"),
    @Json(name = "4")
    AC_DC("AC+DC"),
    @Json(name = "5")
    MMA("MMA")
}


