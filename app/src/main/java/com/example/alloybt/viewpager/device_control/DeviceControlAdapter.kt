package com.example.alloybt.viewpager.device_control

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.json_data.TigValue
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.example.alloybt.viewpager.device_info.DeviceInfoAdapterDelegate
import com.skillbox.multithreading.adapters.DeviceControlAdapterDelegate

class DeviceControlAdapter(
    onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<TigValue>(ParamsDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(DeviceControlAdapterDelegate(onItemClick))
    }

    class ParamsDiffUtilCallBack : DiffUtil.ItemCallback<TigValue>() {
        override fun areItemsTheSame(oldItem: TigValue, newItem: TigValue): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: TigValue, newItem: TigValue): Boolean {
            return oldItem == newItem
        }
    }
}