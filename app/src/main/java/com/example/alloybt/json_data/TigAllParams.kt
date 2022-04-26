package com.example.alloybt

import com.example.alloybt.json_data.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigAllParams(
    @Json(name = "1004")
    val timeGasStart: Float,
    @Json(name = "1005")
    val currentStart: Float,
    @Json(name = "1006")
    val timeToOperateCurrent: Float,
    @Json(name = "1009")
    val freqAC: Float,
    @Json(name = "100B")
    val percentAC: Float,
    @Json(name = "100D")
    val balanceAC: Float,
    @Json(name = "100F")
    val pulseCurrent1: Float,
    @Json(name = "1011")
    val pulseCurrent: Float,
    @Json(name = "1013")
    val freqPulse: Float,
    @Json(name = "1015")
    val currentPulse1: Float,
    @Json(name = "1017")
    val timeCurrentEnd: Float,
    @Json(name = "1019")
    val currentEnd: Float,
    @Json(name = "101B")
    val timeGasEnd: Float,
    @Json(name = "101D")
    val diamElectrode: Float,
    @Json(name = "101F")
    val timeDot: Float,
    @Json(name = "1025")
    val mmaVoltageBreak: Float,
    @Json(name = "1027")
    val mmaArcForce: Float,
    @Json(name = "1029")
    val gasFlowRate: Float,
    @Json(name = "102С")
    val remoteControl: Float,
    @Json(name = "2004")
    val state: TigState,
)







