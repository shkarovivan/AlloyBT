package com.example.alloybt.viewpager.device_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alloybt.R
import com.example.alloybt.json_data.TigStatParams
import com.example.alloybt.databinding.FragmentInfoListBinding
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.example.alloybt.viewmodel.StatViewModel
import com.skillbox.networking.utils.autoCleared
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DeviceInfoFragment : Fragment(R.layout.fragment_info_list) {

    private var _binding: FragmentInfoListBinding? = null
    private val binding get() = _binding!!

    private var infoControlAdapter: DeviceInfoAdapter by autoCleared()

    private val controlViewModel: ControlViewModel by activityViewModels()
    private val statViewModel: StatViewModel by viewModels()

    private var requestStatParams: Boolean = false

    val moshi: Moshi = Moshi.Builder().build()

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

    override fun onResume() {
        super.onResume()
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_INFO)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceivedInfo", btDataReceived)
            parseStatData("$btDataReceived}")

        }
        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestStatParams = (mode == MonitorMode.DEVICE_INFO)
            if (requestStatParams) {
                sendErrorParamsRequest()
            }
        }

        statViewModel.statParams.observe(viewLifecycleOwner){ statParams ->
            infoControlAdapter.items = statParams

        }

        statViewModel.initStatParamsList()

    }

    private fun initList() {
        infoControlAdapter = DeviceInfoAdapter {}

        with(binding.infoListView) {
            adapter = infoControlAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    androidx.recyclerview.widget.RecyclerView.VERTICAL
                )
            )
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }


    private fun parseStatData(data: String) {

        val tigStatParamsAdapter = moshi.adapter(TigStatParams::class.java).nonNull()

        try {
            val tigStatParams = tigStatParamsAdapter.fromJson(data)
            if (tigStatParams != null) {
                Log.d("requestDataReceivedInfo", "parse ok")
                statViewModel.refreshStatParams(tigStatParams)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            Log.d("requestDataReceivedInfo", e.toString())
        }
    }

    private fun getStatJsonString(): String {
        return "{\"Read\":\"Stat\"}"
    }

    private fun sendErrorParamsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (requestStatParams) {
                val jsonString = getStatJsonString()
                controlViewModel.setWeldData(jsonString)
                delay(1000)
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}