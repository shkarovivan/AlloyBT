package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestMonitorParams (
    @Json(name = "Read")
    val type: String = "Monitor",

)

