package com.example.alloybt.viewmodel

import com.example.alloybt.BtDevice
import kotlin.random.Random

class BtDevicesRepository {

	private val initBtDevicesBase: List<BtDevice> = listOf(
		BtDevice(
			model = "MC-315T2 AC/DC",
			macAddress = "DF:JF:JD:23:JF:56:DG",
			seriesNumber = "1342206052\"",
			btSignalLevel = 10,
			modelImageLink = ""
		),
		BtDevice(
			model = "MC-501MX Adaptive",
			macAddress = "DF:JF:JD:23:JF:56:DG",
			seriesNumber = "1802206052",
			btSignalLevel = 30,
			modelImageLink = ""
		),
		BtDevice(
			model = "MC-350MX Pulse",
			macAddress = "DF:JF:JD:23:JF:56:DG",
			seriesNumber = "1592106112",
			btSignalLevel = 1000,
			modelImageLink = ""
		),

	)

	fun initBtDevicesList(): List<BtDevice> {
		var initPhones: List<BtDevice> = emptyList()

		for (i in 1..10) {
			initPhones = initPhones + initBtDevicesBase.random().copy(seriesNumber = Random.nextInt(1342000000,1902112999).toString(), btSignalLevel = Random.nextInt(0,100))
		}
		return initPhones
	}
}