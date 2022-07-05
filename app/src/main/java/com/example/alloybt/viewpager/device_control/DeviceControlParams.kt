package com.example.alloybt.viewpager.device_control

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class DeviceControlParams(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Value")
    val controlParamsList: List<ControlParam>
)
@Parcelize
@JsonClass(generateAdapter = true)
data class ControlParam(
    @Json(name = "Title")
    val title: String,
    @Json(name = "Descr")
    val description: String,
    @Json(name = "Type")
    val type: String,
    @Json(name = "MIN")
    val min: String,
    @Json(name = "MAX")
    val max: String,
    @Json(name = "Value")
    val value: String
): Parcelable