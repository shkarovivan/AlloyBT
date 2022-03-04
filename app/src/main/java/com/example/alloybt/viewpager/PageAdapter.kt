package com.example.alloybt.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceControlFragment
import com.example.alloybt.viewpager.device_control.DeviceControlParamsFragment
import com.example.alloybt.viewpager.device_errors.ErrorsListFragment
import com.example.alloybt.viewpager.device_info.DeviceInfoFragment

class PageAdapter(
    val size: Int,
    activity: ViewPagerFragment,
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BtDeviceControlFragment()
            1 -> DeviceControlParamsFragment()
            2 -> ErrorsListFragment()
            else -> DeviceInfoFragment()
        }
    }

}