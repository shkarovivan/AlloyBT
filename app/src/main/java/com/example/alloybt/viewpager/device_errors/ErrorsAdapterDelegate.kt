package com.skillbox.multithreading.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.adapter.inflate
import com.example.alloybt.viewpager.device_errors.DeviceError
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_error.*


class ErrorsAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<DeviceError, DeviceError, ErrorsAdapterDelegate.ErrorHolder>() {


	override fun isForViewType(item: DeviceError, items: MutableList<DeviceError>, position: Int): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): ErrorHolder {
		return ErrorHolder(parent.inflate(R.layout.item_device_error), onItemClick)
	}

	override fun onBindViewHolder(item: DeviceError, holder: ErrorHolder, payloads: MutableList<Any>) {
		holder.bind(item)
	}

	class ErrorHolder(
		override val containerView: View,
		onItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(containerView), LayoutContainer {

		init {
			containerView.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
		}

		fun bind(error: DeviceError) {
			errorTimeTextView.text = error.time
			errorDescrTextView.text = error.title
		}
	}

}

