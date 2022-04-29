package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestErrorsParams(
    @Json(name = "Read")
    val type: String = "Errors",
    @Json(name = "Page")
    val page: Int
)
