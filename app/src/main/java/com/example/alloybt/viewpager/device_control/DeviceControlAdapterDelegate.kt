package com.skillbox.multithreading.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.utils.inflate
import com.example.alloybt.json_data.ParamType
import com.example.alloybt.json_data.TigValue
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_param.*


class DeviceControlAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<TigValue, TigValue, DeviceControlAdapterDelegate.DeviceControlHolder>() {


	override fun isForViewType(item: TigValue, items: MutableList<TigValue>, position: Int): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): DeviceControlHolder {
		return DeviceControlHolder(parent.inflate(R.layout.item_device_param), onItemClick)
	}

	override fun onBindViewHolder(item: TigValue, holder: DeviceControlHolder, payloads: MutableList<Any>) {
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

		fun bind(tigValue: TigValue) {
			descriptionTextView.text = tigValue.description
			if (tigValue.type == ParamType.ENUM) {
				minHintTextView.visibility = View.GONE
				maxHintTextView.visibility = View.GONE
				minTextView.visibility = View.GONE
				maxTextView.visibility = View.GONE
			}
			else {
				minHintTextView.visibility = View.VISIBLE
				maxHintTextView.visibility = View.VISIBLE
				minTextView.visibility = View.VISIBLE
				maxTextView.visibility = View.VISIBLE
			}
			valueTextView.text = tigValue.value
			maxTextView.text = tigValue.max
			minTextView.text = tigValue.min
		}
	}
}

