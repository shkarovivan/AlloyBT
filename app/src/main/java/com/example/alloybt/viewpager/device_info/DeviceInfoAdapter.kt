package com.example.alloybt.viewpager.device_info

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.json_data.StatParam
import com.example.alloybt.viewpager.device_control.ControlParam
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skillbox.multithreading.adapters.DeviceControlAdapterDelegate

class DeviceInfoAdapter(onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<StatParam>(InfoDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(DeviceInfoAdapterDelegate(onItemClick))
    }

    class InfoDiffUtilCallBack : DiffUtil.ItemCallback<StatParam>() {
        override fun areItemsTheSame(oldItem: StatParam, newItem: StatParam): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: StatParam, newItem: StatParam): Boolean {
            return oldItem == newItem
        }
    }
}