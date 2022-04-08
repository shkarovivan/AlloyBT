package com.example.alloybt.viewmodel

import android.bluetooth.BluetoothDevice
import com.example.alloybt.BtDevice
import com.example.alloybt.R
import com.example.alloybt.viewpager.device_control.ControlParam
import kotlin.random.Random

class BtDevicesRepository {

//	private val initBtDevicesBase: List<BtDevice> = listOf(
//		BtDevice(
//			model = "MC-315T2 AC/DC",
//			macAddress = "DF:JF:JD:23:JF:56:DG",
//			seriesNumber = "1342206052\"",
//			btSignalLevel = 10,
//			modelImageLink = ""
//		),
//		BtDevice(
//			model = "MC-501MX Adaptive",
//			macAddress = "DF:JF:JD:23:JF:56:DG",
//			seriesNumber = "1802206052",
//			btSignalLevel = 30,
//			modelImageLink = ""
//		),
//		BtDevice(
//			model = "MC-350MX Pulse",
//			macAddress = "DF:JF:JD:23:JF:56:DG",
//			seriesNumber = "1592106112",
//			btSignalLevel = 1000,
//			modelImageLink = ""
//		),
//
//		)

	fun initBtDevicesList(): List <BtDevice> = listOf()
//	{
//		var initBtDevices: List<BtDevice> = emptyList()
////
////		for (i in 1..10) {
////			initBtDevices = initBtDevices + initBtDevicesBase.random().copy(seriesNumber = Random.nextInt(1342000000,1902112999).toString(), btSignalLevel = Random.nextInt(0,100))
////		}
//		return initBtDevices
//	}

	fun addBtDevice(model: String, macAddress: String, number: String, btSignalLevel: Int, btDevice: BluetoothDevice): BtDevice {
		return BtDevice (model, macAddress, number, btSignalLevel, "", btDevice)
	}
}