package com.example.alloybt.viewpager.device_control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.MainActivity
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.databinding.FragmentDeviceParamsListBinding
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewpager.ViewPagerFragmentDirections
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlin.random.Random

class DeviceControlParamsFragment: Fragment(R.layout.fragment_device_params_list) {

    private var _binding: FragmentDeviceParamsListBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()

    private var deviceControlAdapter: DeviceControlAdapter by autoCleared()

    //lateinit var deviceParamsList: List<ControlParam>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentDeviceParamsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
      //  setExampleDeviceControlList()

       // deviceControlAdapter.items = deviceParamsList

        binding.refreshParams.setOnClickListener {

        }

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            binding.testTextView.text = btDataReceived
            //toast(btDataReceived)

        }
    }

    private fun initList(){
        deviceControlAdapter= DeviceControlAdapter { position ->

            val param = deviceControlAdapter.items[position]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToTuneParamFragment(param)
            findNavController().navigate(action)
        }

        with(binding.paramsListView) {
            adapter = deviceControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun bindViewModel(){

    }

    private fun setExampleDeviceControlList(){

        val deviceParamsList = mutableListOf<ControlParam>()
        for (i in 0..7){
            val max = Random.nextInt(5, 30 )
            deviceParamsList +=  listOf(
                ControlParam(
                    title = Random.nextInt(1,1000).toString(),
                    description = resources.getString(R.string.default_param_description_text),
                    type = "Float",
                    min = "0.0",
                    max = max.toString(),
                    value = Random.nextInt(0, max ).toString()
                ))
        }
    }

    private fun toast(text: String){
        Toast.makeText(requireContext(),text, Toast.LENGTH_SHORT).show()
    }
}