package com.example.alloybt.adapter

import android.view.View
import android.view.ViewGroup
import com.example.alloybt.BtDevice
import com.example.alloybt.R
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class BtDevicesAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
	private val onLongItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<BtDevice, BtDevice, BtDevicesAdapterDelegate.BluetoothDevicesHolder>() {

	override fun isForViewType(
		item: BtDevice,
		items: MutableList<BtDevice>,
		position: Int,
	): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): BluetoothDevicesHolder {
		return BluetoothDevicesHolder(parent.inflate(R.layout.item_bluetooth_device),
			onItemClick,
			onLongItemClick)
	}

	override fun onBindViewHolder(
		item: BtDevice,
		holder: BluetoothDevicesHolder,
		payloads: MutableList<Any>,
	) {
		holder.bind(item)
	}


	class BluetoothDevicesHolder(
		containerView: View,
		onItemClick: (position: Int) -> Unit,
		onLongItemClick: (position: Int) -> Unit,
	) : BtDevicesHolder(containerView, onItemClick, onLongItemClick) {

		fun bind(btDevice: BtDevice) {
			bindInfo(btDevice.model,
				btDevice.macAddress,
				btDevice.seriesNumber,
				btDevice.btSignalLevel,
				btDevice.modelImageLink)
		}
	}
}