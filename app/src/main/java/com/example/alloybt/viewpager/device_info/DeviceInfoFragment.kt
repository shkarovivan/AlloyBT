package com.example.alloybt.viewpager.device_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentInfoListBinding
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class DeviceInfoFragment: Fragment(R.layout.fragment_info_list) {

    private var _binding: FragmentInfoListBinding? = null
    private val binding get() = _binding!!
    private var infoControlAdapter: DeviceInfoAdapter by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        setExampleDeviceInfolList()
    }

    private fun initList(){
        infoControlAdapter= DeviceInfoAdapter { position ->
//            val param = btDevicesListViewModel.btDevicesList.value?.get(position)!!
//            //val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToBtDeviceControl(btDeviceInformation)
//            val action = SearchDevicesFragmentDirections.actionSearchDevicesFragmentToViewPagerFragment(btDeviceInformation)
//            findNavController().navigate(action)
        }

        with(binding.infoListView) {
            adapter = infoControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), androidx.recyclerview.widget.RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun setExampleDeviceInfolList(){

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