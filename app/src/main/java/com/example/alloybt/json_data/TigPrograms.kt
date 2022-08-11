package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TigPrograms(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Page")
    val page: Int,
    @Json(name = "Value")
    val value: List<TigProgram>
)

@JsonClass(generateAdapter = true)
data class TigProgram(
    @Json(name = "N")
    val number: Int,
    @Json(name = "M")
    val mode: Int,
    @Json(name = "I")
    val current: Int,
    @Json(name = "B")
    val buttonMode: Int
)

val tigProgramMode = mapOf(
    0 to "AC",
    1 to "AC Pulse",
    2 to "DC",
    3 to "DC Pulse",
    4 to "AC+DC",
    5 to "MMA",
    255 to "пусто"
)

val tigButtonMode = mapOf(
    0 to "2T",
    1 to "4T",
    2 to "Spot",
    3 to "Repeat",
)

