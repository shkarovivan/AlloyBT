package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigMonitorParams(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Value")
    val value: FastParams
)

@JsonClass(generateAdapter = true)
data class FastParams(
    @Json(name = "1000")
    val mode: Int, //TigMode,
    @Json(name = "1001")
    val liftTig: Int, //LiftTig,
    @Json(name = "1002")
    val weldButtonMode: Int, //WeldButtonMode,
    @Json(name = "1003")
    val waveForm: Int,
    @Json(name = "1007")
    val workCurrent: Int, // Float,
    @Json(name = "101D")
    val diamElectrode: Float,
    @Json(name = "2000")
    val realCurrent: Float,
    @Json(name = "2002")
    val realVoltage: Float,
    @Json(name = "2004")
    val state: Int, //TigState,
    @Json(name = "2005")
    val errors: Int, //TigErrors,
)

    @JsonClass(generateAdapter = true)
    data class TigFastParams(
    @Json(name = "1000")
    val mode: TigMode,
    @Json(name = "1001")
    val liftTig: LiftTig,
    @Json(name = "1002")
    val weldButtonMode: WeldButtonMode,
    @Json(name = "1003")
    val waveForm: Int,
    @Json(name = "1007")
    val workCurrent: Float,
    @Json(name = "101D")
    val diamElectrode: Float,
    @Json(name = "2000")
    val realCurrent: Float,
    @Json(name = "2002")
    val realVoltage: Float,
    @Json(name = "2004")
    val state: TigState,
    @Json(name = "2005")
    val errors: Int,
)
