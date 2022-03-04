package com.skillbox.multithreading.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.viewpager.device_errors.DeviceError
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ErrorsAdapter(
	onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<DeviceError>(MoviesDiffUtilCallBack()) {

	init {
		delegatesManager.addDelegate(ErrorsAdapterDelegate(onItemClick))
	}
	class MoviesDiffUtilCallBack : DiffUtil.ItemCallback<DeviceError>() {
		override fun areItemsTheSame(oldItem: DeviceError, newItem: DeviceError): Boolean {
			return oldItem.time == newItem.time && oldItem.title == newItem.title
		}


		override fun areContentsTheSame(oldItem: DeviceError, newItem: DeviceError): Boolean {
			return oldItem == newItem
		}
	}
}