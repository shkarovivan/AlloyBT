package com.example.alloybt

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BtDevice(
	val model: String,
	val macAddress: String,
	val seriesNumber: String,
	val btSignalLevel: Int,
	val modelImageLink: String,
	val device: BluetoothDevice
): Parcelable
