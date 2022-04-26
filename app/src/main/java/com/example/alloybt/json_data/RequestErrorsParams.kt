package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestErrorsParams(
    @Json(name = "Type")
    val type: String = "Read",
    @Json(name = "Value")
    val value: String = "Errors"
)
