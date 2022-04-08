package com.skillbox.multithreading.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.adapter.inflate
import com.example.alloybt.viewpager.device_control.ControlParam
import com.example.alloybt.viewpager.device_info.DeviceInfoAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_param.*


class DeviceControlAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<ControlParam, ControlParam, DeviceControlAdapterDelegate.DeviceControlHolder>() {


	override fun isForViewType(item: ControlParam, items: MutableList<ControlParam>, position: Int): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): DeviceControlHolder {
		return DeviceControlHolder(parent.inflate(R.layout.item_device_param), onItemClick)
	}

	override fun onBindViewHolder(item: ControlParam, holder: DeviceControlHolder, payloads: MutableList<Any>) {
		holder.bind(item)
	}

	class DeviceControlHolder(
		override val containerView: View,
		onItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(containerView), LayoutContainer {

		init {
			containerView.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
		}

		fun bind(controlParam: ControlParam) {
			descriptionTextView.text = controlParam.description
			if (controlParam.type == "String") {
				minHintTextView.isVisible = false
				maxHintTextView.isVisible = false
				minTextView.isVisible = false
				maxTextView.isVisible = false
			}
			valueTextView.text = controlParam.value
			maxTextView.text = controlParam.max
			minTextView.text = controlParam.min

		}
	}
}

