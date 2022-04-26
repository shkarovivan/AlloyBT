package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class TigState( mode: String ){
    @Json(name = "0")
    BUTTON_OFF("Button OFF"),
    @Json(name = "1")
    BUTTON_ON("Robot ON"),
    @Json(name = "2")
    ARC_ON("Arc ON"),
}
