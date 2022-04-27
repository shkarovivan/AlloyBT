package com.example.alloybt

import com.example.alloybt.json_data.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigControlParams(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Value")
    val value: TigAllParams
)

@JsonClass(generateAdapter = true)
data class TigAllParams(
    @Json(name = "1004")
    val timeGasStart: Float,
    @Json(name = "1005")
    val currentStart: Int,
    @Json(name = "1006")
    val timeToOperateCurrent: Float,
    @Json(name = "1009")
    val freqAC: Int,
    @Json(name = "100B")
    val percentAC: Int,
    @Json(name = "100D")
    val balanceAC: Int,
    @Json(name = "100F")
    val pulseCurrent1: Int,
    @Json(name = "1011")
    val pulseCurrent: Int,
    @Json(name = "1013")
    val freqPulse: Int,
    @Json(name = "1015")
    val currentPulse1: Int,
    @Json(name = "1017")
    val timeCurrentEnd: Float,
    @Json(name = "1019")
    val currentEnd: Int,
    @Json(name = "101B")
    val timeGasEnd: Float,
    @Json(name = "101F")
    val timeDot: Float,
    @Json(name = "1025")
    val mmaVoltageBreak: Int,
    @Json(name = "1027")
    val mmaArcForce: Int,
    @Json(name = "1029")
    val gasFlowRate: Int,
    @Json(name = "102C")
    val remoteControl: Int,
)







