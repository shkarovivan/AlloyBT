package com.example.alloybt.viewpager.device_monitor

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeldMonitorParams(
    @Json(name = "Response")
    val Response: String,
    @Json(name = "Value")
    val value: WeldParams,
)

@JsonClass(generateAdapter = true)
data class WeldParams(
@Json(name = "WeldType")
val weldType: String,
@Json(name = "TorchControl")
val torchControl: String,
@Json(name = "Material")
val material: String,
@Json(name = "WireDiameter")
val wireDiameter: String,
@Json(name = "GasType")
val gasType: String,
@Json(name = "CurrentValue")
val currentValue: String,
@Json(name = "VoltageValue")
val voltageValue: String
)
