package com.example.alloybt.viewpager.device_control

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.MainActivity
import com.example.alloybt.R
import com.example.alloybt.TigAllParams
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.databinding.FragmentDeviceParamsListBinding
import com.example.alloybt.json_data.RequestAllParams
import com.example.alloybt.json_data.RequestMonitorParams
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.example.alloybt.viewpager.ViewPagerFragmentDirections
import com.example.alloybt.viewpager.device_monitor.WeldMonitorParams
import com.skillbox.networking.utils.autoCleared
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import no.nordicsemi.android.ble.livedata.state.ConnectionState

class DeviceControlParamsFragment : Fragment(R.layout.fragment_device_params_list) {

    private var _binding: FragmentDeviceParamsListBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()

    private var deviceControlAdapter: DeviceControlAdapter by autoCleared()
    private var requestControlParams: Boolean = false
    private var isReady: Boolean = false

    val moshi: Moshi = Moshi.Builder().build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceParamsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onResume() {
        super.onResume()
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_CONTROL)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.coroutineContext.cancelChildren()
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
            parseMonitorData(btDataReceived)
        }

        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestControlParams = (mode == MonitorMode.DEVICE_CONTROL)
            if (requestControlParams) {
                sendControlParamsRequest()
                toast("$mode")
            }
        }

        controlViewModel.connectionState.observe(viewLifecycleOwner) {
            if (it.state == ConnectionState.State.READY) {
                isReady = true
            }
        }
    }

    private fun initList() {
        deviceControlAdapter = DeviceControlAdapter { position ->
//
//            val param = deviceControlAdapter.items[position]
//            val action =
//                ViewPagerFragmentDirections.actionViewPagerFragmentToTuneParamFragment(param)
//            findNavController().navigate(action)
        }

        with(binding.paramsListView) {
            adapter = deviceControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun bindViewModel() {

    }

    private fun setExampleDeviceControlList() {

        val deviceParamsList = mutableListOf<ControlParam>()
        for (i in 0..7) {
            val max = Random.nextInt(5, 30)
            deviceParamsList += listOf(
                ControlParam(
                    title = Random.nextInt(1, 1000).toString(),
                    description = resources.getString(R.string.default_param_description_text),
                    type = "Float",
                    min = "0.0",
                    max = max.toString(),
                    value = Random.nextInt(0, max).toString()
                )
            )
        }
    }

    private fun sendText(data: String) {
        controlViewModel.setWeldData(data)
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun parseMonitorData(data: String) {

        val tigAllParamsAdapter = moshi.adapter(TigAllParams::class.java).nonNull()

        try {
            val tigAllParams = tigAllParamsAdapter.fromJson(data)
            if (tigAllParams != null) {

              //  showParams(weldParams)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            //toast(e.toString())
        }
    }

    private fun sendControlParamsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("requestData", "lifecycleScope start")
            val adapter = moshi.adapter(RequestAllParams::class.java).nonNull()
            var requestControlJson = ""
            try {
                requestControlJson = adapter.toJson(RequestAllParams())
            } catch (e: Exception) {
                // toast("movie to JSON error = ${e.message}")
            }
            if (requestControlParams && isReady) {
                (0..3).forEach { _ ->
                    sendText(requestControlJson)
                    Log.d("requestData", requestControlJson)
                    delay(2000)
                }
            }
        }
    }
}
