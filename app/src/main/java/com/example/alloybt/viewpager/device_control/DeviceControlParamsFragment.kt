package com.example.alloybt.viewpager.device_control

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_errors_list.*
import kotlin.random.Random

class DeviceControlParamsFragment: Fragment(R.layout.fragment_device_params_list) {

    private var deviceControlAdapter: DeviceControlAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        setExampleDeviceControlList()
    }

    private fun initList(){
        deviceControlAdapter= DeviceControlAdapter { position ->
//            val param = btDevicesListViewModel.btDevicesList.value?.get(position)!!
//            //val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToBtDeviceControl(btDeviceInformation)
//            val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToViewPagerFragment(btDeviceInformation)
//            findNavController().navigate(action)
        }

        with(paramsListView) {
            adapter = deviceControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun setExampleDeviceControlList(){

        val deviceParamsList = mutableListOf<ControlParam>()
        for (i in 0..10){
            deviceParamsList +=  listOf(
                ControlParam(
                    title = Random.nextInt(1,1000).toString(),
                    description = resources.getString(R.string.default_param_description_text),
                    type = "Float",
                    min = "0.0",
                    max = Random.nextInt(5, 30 ).toString(),
                    value = Random.nextInt(0, 30 ).toString()
                ))
        }

        deviceControlAdapter.items = deviceParamsList
    }
}