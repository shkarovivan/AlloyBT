package com.example.alloybt.viewpager.device_control

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.example.alloybt.viewpager.device_info.DeviceInfoAdapterDelegate
import com.skillbox.multithreading.adapters.DeviceControlAdapterDelegate

class DeviceControlAdapter(
    onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<ControlParam>(ParamsDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(DeviceControlAdapterDelegate(onItemClick))
    }

    class ParamsDiffUtilCallBack : DiffUtil.ItemCallback<ControlParam>() {
        override fun areItemsTheSame(oldItem: ControlParam, newItem: ControlParam): Boolean {
            return oldItem.title == newItem.title && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ControlParam, newItem: ControlParam): Boolean {
            return oldItem == newItem
        }
    }
}