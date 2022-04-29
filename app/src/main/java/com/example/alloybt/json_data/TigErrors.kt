package com.example.alloybt.json_data

import com.example.alloybt.TigAllParams
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TigControlParams(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Page")
    val page: Int,
    @Json(name = "Value")
    val value: List<TigError>
)

@JsonClass(generateAdapter = true)
data class TigError(
    @Json(name = "Num")
    val num: Int,
    @Json(name = "Time")
    val time: Int,
    @Json(name = "Level")
    val level: Int,
    @Json(name = "Code")
    val code: Int
)



