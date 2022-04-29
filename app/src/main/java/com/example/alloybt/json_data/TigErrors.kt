package com.example.alloybt.json_data

import com.example.alloybt.TigAllParams
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TigErrors(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Page")
    val page: Int,
    @Json(name = "Value")
    val value: List<TigError>
)

@JsonClass(generateAdapter = true)
data class TigError(
    @Json(name = "N")
    val num: Int,
    @Json(name = "T")
    val time: Int,
    @Json(name = "L")
    val level: Int,
    @Json(name = "C")
    val code: Int
)



