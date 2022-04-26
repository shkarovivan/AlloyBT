package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestPassword(
    @Json(name = "Type")
    val type: String = "Password",
    @Json(name = "Value")
    val value: String
)
