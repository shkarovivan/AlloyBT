package com.example.alloybt.viewpager.device_program

import androidx.recyclerview.widget.DiffUtil
import com.example.alloybt.json_data.TigProgram
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DeviceProgramAdapter (
    onSaveClick: (position: Int) -> Unit,
    onLoadClick: (position: Int) -> Unit

) : AsyncListDifferDelegationAdapter<TigProgram>(ProgramsDiffUtilCallBack()) {

    init {
        delegatesManager.addDelegate(DeviceProgramAdapterDelegate(onSaveClick,onLoadClick))
    }

    class ProgramsDiffUtilCallBack : DiffUtil.ItemCallback<TigProgram>() {
        override fun areItemsTheSame(oldItem: TigProgram, newItem: TigProgram): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: TigProgram, newItem: TigProgram): Boolean {
            return oldItem == newItem
        }
    }
}