package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigStatParams(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Value")
    val value: StatParams
)

@JsonClass(generateAdapter = true)
data class StatParams(
    @Json(name = "3000")
    val workTimeAll: Int,
    @Json(name = "3001")
    val weldTimeAll: Int,
    @Json(name = "3002")
    val workTimeLast: Int,
    @Json(name = "3003")
    val weldTimeLast: Int,
    @Json(name = "3010")
    val bvoMotorFreq: Float,
    @Json(name = "3011")
    val bvoFlowSpeed: Float,
    @Json(name = "3012")
    val bvoTempBefore: Float,
    @Json(name = "3013")
    val bvoTempAfter: Float,
    @Json(name = "3014")
    val bvoTempMotor: Float,
)







