package com.example.alloybt.viewpager.device_info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.viewpager.device_control.ControlParam
import com.example.alloybt.viewpager.device_control.DeviceControlAdapter
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_errors_list.*
import kotlinx.android.synthetic.main.fragment_errors_list.paramsListView
import kotlinx.android.synthetic.main.fragment_info_list.*
import kotlin.random.Random

class DeviceInfoFragment: Fragment(R.layout.fragment_info_list) {
    private var infoControlAdapter: DeviceInfoAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        setExampleDeviceControlList()
    }

    private fun initList(){
        infoControlAdapter= DeviceInfoAdapter { position ->
//            val param = btDevicesListViewModel.btDevicesList.value?.get(position)!!
//            //val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToBtDeviceControl(btDeviceInformation)
//            val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToViewPagerFragment(btDeviceInformation)
//            findNavController().navigate(action)
        }

        with(infoListView) {
            adapter = infoControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), androidx.recyclerview.widget.RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun setExampleDeviceControlList(){

        val infoParamsList = mutableListOf<InfoParam>()
        for (i in 0..15){
            infoParamsList +=  listOf(
                InfoParam(
                    title = resources.getString(R.string.default_info_description_text)+"$i",
                    value = resources.getString(R.string.default_info_text)
                )
            )
        }
        infoControlAdapter.items = infoParamsList
    }
}