package com.example.alloybt.viewpager.device_info

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class DeviceInfo(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Value")
    val controlParamsList: List<InfoParam>
)

@JsonClass(generateAdapter = true)
data class InfoParam(
    @Json(name = "Title")
    val title: String,
    @Json(name = "Value")
    val value: String
)