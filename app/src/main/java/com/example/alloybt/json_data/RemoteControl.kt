package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class RemoteControl( mode: String ){
    @Json(name = "0")
    OFF("Robot OFF"),
    @Json(name = "1")
    On("Robot ON"),
}