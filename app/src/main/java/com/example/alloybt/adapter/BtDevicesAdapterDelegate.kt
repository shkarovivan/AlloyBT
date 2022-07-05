package com.example.alloybt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alloybt.BtDevice
import com.example.alloybt.R
import com.example.alloybt.databinding.ItemBluetoothDeviceBinding
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
		val itemBinding = ItemBluetoothDeviceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
		return BluetoothDevicesHolder(itemBinding,
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
		private val binding: ItemBluetoothDeviceBinding,
		onItemClick: (position: Int) -> Unit,
		onLongItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		init {
//		containerView.setOnClickListener {
//			onItemClick(bindingAdapterPosition)
//		}
			binding.inverterImage.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
			binding.root.setOnLongClickListener {
				onLongItemClick(bindingAdapterPosition)
				return@setOnLongClickListener true
			}
		}

		fun bind(btDevice: BtDevice) {
			binding.inverterModelTextView.text = btDevice.model
			binding.macAddressTextView.text = btDevice.macAddress
			binding.seriesNumberTextView.text = btDevice.seriesNumber

			val signalLevelPerCent = ((100.0f * (127.0f + btDevice.btSignalLevel) / (127.0f + 20.0f))).toInt()
			binding.rssi.setImageLevel(signalLevelPerCent)

			Log.e("BluetoothRSSI", "RSSI:  ${btDevice.btSignalLevel} - $signalLevelPerCent ")
			Glide.with(itemView)
				//.load(modelImageLink)
				.load(R.drawable.t3acdc_logo)
				.placeholder(R.drawable.ic_image_not_supported)
				.into(binding.inverterImage)
		}
	}
}