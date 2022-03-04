package com.example.alloybt.viewpager.device_info

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.adapter.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_info.*


class DeviceInfoAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<InfoParam, InfoParam, DeviceInfoAdapterDelegate.DeviceInfoHolder>() {


	override fun isForViewType(item: InfoParam, items: MutableList<InfoParam>, position: Int): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): DeviceInfoHolder {
		return DeviceInfoHolder(parent.inflate(R.layout.item_device_info), onItemClick)
	}

	override fun onBindViewHolder(item: InfoParam, holder: DeviceInfoHolder, payloads: MutableList<Any>) {
		holder.bind(item)
	}

	class DeviceInfoHolder(
		override val containerView: View,
		onItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(containerView), LayoutContainer {

		init {
			containerView.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
		}

		fun bind(infoParam: InfoParam) {
			infoTitleTextView.text = infoParam.title
			infoTimeTextView.text = infoParam.value
		}
	}
}

