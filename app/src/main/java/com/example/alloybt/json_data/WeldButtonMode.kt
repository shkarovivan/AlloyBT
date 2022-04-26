package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class WeldButtonMode(mode: String) {
    @Json(name = "0")
    M2T("2T"),
    @Json(name = "1")
    M4T("4T"),
    @Json(name = "2")
    SPOT("SPOT"),
    @Json(name = "3")
    REPEAT("REPEAT")
}