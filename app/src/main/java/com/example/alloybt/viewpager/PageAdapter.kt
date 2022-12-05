package com.example.alloybt.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment
import com.example.alloybt.viewpager.device_control.DeviceControlParamsFragment
import com.example.alloybt.viewpager.device_errors.ErrorsListFragment
import com.example.alloybt.viewpager.device_info.DeviceInfoFragment
import com.example.alloybt.viewpager.device_program.ProgramsListFragment
import com.example.alloybt.viewpager.graph_param.GraphParamFragment

class PageAdapter(
    private val size: Int,
    activity: ViewPagerFragment,
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BtDeviceMonitorFragment()
            1 -> DeviceControlParamsFragment()
            2 -> GraphParamFragment()
            3 -> ErrorsListFragment()
            4 -> DeviceInfoFragment()
            //  4 -> ProgramsListFragment()
            else -> BtDeviceMonitorFragment()
        }
    }

}