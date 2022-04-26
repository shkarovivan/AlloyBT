package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RequestWriteParams(
    @Json(name = "Write")
    val value: WriteParamValue = WriteParamValue()
)

@JsonClass(generateAdapter = true)
data class WriteParamValue(
    @Json(name = "1007")
    var value: Int  = 0
)