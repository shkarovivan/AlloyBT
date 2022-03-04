package com.example.alloybt.viewpager.device_errors

data class DeviceErrors(
    val response: String,
    val errorsList: List<DeviceError>
)

data class DeviceError(
    val title: String,
    val time: String
)