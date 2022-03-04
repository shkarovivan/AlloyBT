package com.example.alloybt.viewpager.device_info

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.viewpager.device_control.ControlParam
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.skillbox.multithreading.adapters.DeviceControlAdapterDelegate

class DeviceInfoAdapter(onItemClick: (position: Int) -> Unit,
) : AsyncListDifferDelegationAdapter<InfoParam>(InfoDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(DeviceInfoAdapterDelegate(onItemClick))
    }

    class InfoDiffUtilCallBack : DiffUtil.ItemCallback<InfoParam>() {
        override fun areItemsTheSame(oldItem: InfoParam, newItem: InfoParam): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: InfoParam, newItem: InfoParam): Boolean {
            return oldItem == newItem
        }
    }
}