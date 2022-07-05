package com.example.alloybt.viewpager.device_info

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.databinding.ItemDeviceInfoBinding
import com.example.alloybt.json_data.StatParam
import com.example.alloybt.json_data.StatType
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class DeviceInfoAdapterDelegate(
    private val onItemClick: (position: Int) -> Unit,
) : AbsListItemAdapterDelegate<StatParam, StatParam, DeviceInfoAdapterDelegate.DeviceInfoHolder>() {


    override fun isForViewType(
        item: StatParam,
        items: MutableList<StatParam>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): DeviceInfoHolder {
        val itemBinding = ItemDeviceInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeviceInfoHolder(itemBinding, onItemClick)
    }

    override fun onBindViewHolder(
        item: StatParam,
        holder: DeviceInfoHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    class DeviceInfoHolder(
        private val binding: ItemDeviceInfoBinding,
        onItemClick: (position: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(statParam: StatParam) {
            binding.infoTitleTextView.text = statParam.description
            if (statParam.type == StatType.TIME) {
                val hours = statParam.value.toInt()/3600
                val minutes = (statParam.value.toInt()-(hours*3600))/60
                val seconds = statParam.value.toInt()-(hours*3600) -(minutes*60)
                val minutesString = if (minutes<10) {"0$minutes"}
                else {"$minutes"}
                val secondsString = if (seconds<10) {"0$seconds"}
                else {"$seconds"}

                binding.infoTimeTextView.text = "$hours:$minutesString:$secondsString"
            } else {
                binding.infoTimeTextView.text = statParam.value.toString()
            }
            binding.infoUnitTextView.text = statParam.unit
        }
    }
}

