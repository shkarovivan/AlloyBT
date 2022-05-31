package com.skillbox.multithreading.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.json_data.TigError
import com.example.alloybt.viewpager.device_errors.DeviceError
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ErrorsAdapter(
	onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<TigError>(MoviesDiffUtilCallBack()) {

	init {
		delegatesManager.addDelegate(ErrorsAdapterDelegate(onItemClick))
	}
	class MoviesDiffUtilCallBack : DiffUtil.ItemCallback<TigError>() {
		override fun areItemsTheSame(oldItem: TigError, newItem: TigError): Boolean {
			return oldItem.num == newItem.num
		}


		override fun areContentsTheSame(oldItem: TigError, newItem: TigError): Boolean {
			return oldItem == newItem
		}
	}
}