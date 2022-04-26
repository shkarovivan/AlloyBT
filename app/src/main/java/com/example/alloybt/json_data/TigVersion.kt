package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigVersions(
    @Json(name = "0001")
    val protocolVersion: String,
    @Json(name = "0002")
    val mainBoardVersion: String,
    @Json(name = "0003")
    val tftBoardVersion: String,
    @Json(name = "0004")
    val tftBoardDate: String,
)