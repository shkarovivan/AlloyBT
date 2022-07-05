package com.skillbox.multithreading.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.databinding.ItemDeviceParamBinding
import com.example.alloybt.json_data.ParamType
import com.example.alloybt.json_data.TigValue
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class DeviceControlAdapterDelegate(
	private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<TigValue, TigValue, DeviceControlAdapterDelegate.DeviceControlHolder>() {


	override fun isForViewType(item: TigValue, items: MutableList<TigValue>, position: Int): Boolean {
		return true
	}

	override fun onCreateViewHolder(parent: ViewGroup): DeviceControlHolder {
		val itemBinding = ItemDeviceParamBinding.inflate(LayoutInflater.from(parent.context),parent,false)
		return DeviceControlHolder(itemBinding , onItemClick)
	}

	override fun onBindViewHolder(item: TigValue, holder: DeviceControlHolder, payloads: MutableList<Any>) {
		holder.bind(item)
	}

	class DeviceControlHolder(
		private val binding: ItemDeviceParamBinding,
		onItemClick: (position: Int) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		init {
			binding.root.setOnClickListener {
				onItemClick(bindingAdapterPosition)
			}
		}

		fun bind(tigValue: TigValue) {
			binding.descriptionTextView.text = tigValue.description
			if (tigValue.type == ParamType.ENUM) {
				binding.minHintTextView.visibility = View.GONE
				binding.maxHintTextView.visibility = View.GONE
				binding.minTextView.visibility = View.GONE
				binding.maxTextView.visibility = View.GONE
			}
			else {
				binding.minHintTextView.visibility = View.VISIBLE
				binding.maxHintTextView.visibility = View.VISIBLE
				binding.minTextView.visibility = View.VISIBLE
				binding.maxTextView.visibility = View.VISIBLE
			}
			binding.valueTextView.text = tigValue.value
			binding.maxTextView.text = tigValue.max
			binding.minTextView.text = tigValue.min
		}
	}
}

