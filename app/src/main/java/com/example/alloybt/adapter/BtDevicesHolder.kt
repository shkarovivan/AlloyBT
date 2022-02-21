package com.example.alloybt.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import com.example.alloybt.R
import kotlinx.android.synthetic.main.item_bluetooth_device.*


abstract class BtDevicesHolder(
	final override val containerView: View,
	onItemClick: (position: Int) -> Unit,
	onLongItemClick: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

	init {
//		containerView.setOnClickListener {
//			onItemClick(bindingAdapterPosition)
//		}
		val imageView = containerView.findViewById<ImageView>(R.id.inverterImage)
		imageView.setOnClickListener {
			onItemClick(bindingAdapterPosition)
		}
		containerView.setOnLongClickListener {
			onLongItemClick(bindingAdapterPosition)
			return@setOnLongClickListener true
		}
	}

	protected fun bindInfo(
		model: String,
		macAddress: String,
		seriesNumber: String,
		btSignalLevel: Int,
		modelImageLink: String,
	) {
		inverterModelTextView.text = model
		macAddressTextView.text = macAddress
		seriesNumberTextView.text = seriesNumber

		val signalLevelPerCent = ((100.0f * (127.0f + btSignalLevel) / (127.0f + 20.0f))).toInt()
		//rssi.setImageLevel(btSignalLevel)
		rssi.setImageLevel(signalLevelPerCent)

		Log.e("BluetoothRSSI", "RSSI:  ${btSignalLevel} - $signalLevelPerCent ")
		Glide.with(itemView)
			//.load(modelImageLink)
			.load(R.drawable.t3acdc_logo)
			.placeholder(R.drawable.ic_image_not_supported)
			.into(inverterImage)
	}

}
