package com.example.alloybt.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.BtDevice
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class BtDevicesAdapter(
	onItemClick: (position: Int) -> Unit,
	onLongItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<BtDevice>(PhonesDiffUtilCallBack()) {


	init {
		delegatesManager.addDelegate(BtDevicesAdapterDelegate(onItemClick, onLongItemClick))
	}

	class PhonesDiffUtilCallBack : DiffUtil.ItemCallback<BtDevice>() {
		override fun areItemsTheSame(oldItem: BtDevice, newItem: BtDevice): Boolean {
			return oldItem.macAddress == newItem.macAddress && oldItem.seriesNumber == newItem.seriesNumber
		}

		override fun areContentsTheSame(oldItem: BtDevice, newItem: BtDevice): Boolean {
			return oldItem == newItem
		}
	}
}