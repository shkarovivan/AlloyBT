package com.example.alloybt.viewpager.device_info

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.utils.inflate
import com.example.alloybt.json_data.StatParam
import com.example.alloybt.json_data.StatType
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_device_info.*


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
        return DeviceInfoHolder(parent.inflate(R.layout.item_device_info), onItemClick)
    }

    override fun onBindViewHolder(
        item: StatParam,
        holder: DeviceInfoHolder,
        payloads: MutableList<Any>
    ) {
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

        @SuppressLint("SetTextI18n")
        fun bind(statParam: StatParam) {
            infoTitleTextView.text = statParam.description
            if (statParam.type == StatType.TIME) {
                val hours = statParam.value.toInt()/3600
                val minutes = (statParam.value.toInt()-(hours*3600))/60
                val seconds = statParam.value.toInt()-(hours*3600) -(minutes*60)
                val minutesString = if (minutes<10) {"0$minutes"}
                else {"$minutes"}
                val secondsString = if (seconds<10) {"0$seconds"}
                else {"$seconds"}

                infoTimeTextView.text = "$hours:$minutesString:$secondsString"
            } else {
                infoTimeTextView.text = statParam.value.toString()
            }
            infoUnitTextView.text = statParam.unit
        }
    }
}

