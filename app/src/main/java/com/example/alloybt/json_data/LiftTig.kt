package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class LiftTig(mode: String) {
    @Json(name = "0")
    OSC("OSC"),
    @Json(name = "1")
    LIFT_TIG("LiftTIG")
}