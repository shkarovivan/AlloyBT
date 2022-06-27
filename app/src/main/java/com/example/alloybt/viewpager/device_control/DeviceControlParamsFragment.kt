package com.example.alloybt.viewpager.device_control

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.TigControlParams
import com.example.alloybt.databinding.FragmentDeviceParamsListBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.utils.showAlertDialog
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.example.alloybt.viewmodel.ParamsViewModel
import com.example.alloybt.viewpager.ViewPagerFragmentDirections
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment
import com.skillbox.networking.utils.autoCleared
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.livedata.state.ConnectionState
import org.json.JSONObject

class DeviceControlParamsFragment : Fragment(R.layout.fragment_device_params_list) {

    private var _binding: FragmentDeviceParamsListBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()
    private val paramsViewModel: ParamsViewModel by activityViewModels()

    private var deviceControlAdapter: DeviceControlAdapter by autoCleared()
    private var requestControlParams: Boolean = false
    private var isReady: Boolean = false
    private var userPassword: String? = null

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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        paramsViewModel.initParamsList()
        binding.refreshParams.setOnClickListener {
            sendControlParamsRequest()
        }

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceived2", btDataReceived)
            parseMonitorData(btDataReceived)

        }

        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestControlParams = (mode == MonitorMode.DEVICE_CONTROL)
            if (requestControlParams) {
                sendControlParamsRequest()
            }
        }

        controlViewModel.connectionState.observe(viewLifecycleOwner) {
            isReady = it.state == ConnectionState.State.READY
        }

        paramsViewModel.params.observe(viewLifecycleOwner) { tigParamsList ->
            deviceControlAdapter.items = tigParamsList
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initList() {
        deviceControlAdapter = DeviceControlAdapter { position ->
//
            if (Password.token == null) {
                requestToken()
            } else {
                val tigValue = deviceControlAdapter.items[position]
                if (tigValue.type != ParamType.ENUM) {
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomTune(
                            tigValue
                        )
                    findNavController().navigate(action)
                } else {
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(
                            tigValue
                        )
                    findNavController().navigate(action)
                }
            }

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

    private fun sendText(data: String) {
        controlViewModel.setWeldData(data)
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun parseMonitorData(data: String) {

        val tigAllParamsAdapter = moshi.adapter(TigControlParams::class.java).nonNull()

        try {
            val tigAllParams = tigAllParamsAdapter.fromJson(data)
            if (tigAllParams != null) {
                Log.d("requestDataReceived2", "parse ok")
                paramsViewModel.refreshAllParams(tigAllParams.value)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            try {
                val jsonObject = JSONObject(data)
                val token = jsonObject.getLong("Token")
                Password.token = token
                if (userPassword != null) {
                    Password.password = userPassword
                    savePassword(userPassword!!)
                }
                toast(getString(R.string.token_success_text))
                Log.d("token", "token ok")
            } catch (e: Exception) {
                try {
                    val jsonObject = JSONObject(data)
                    val token = jsonObject.getString("Token")
                    if (token == "Error") {
                        Password.password = null
                        toast(getString(R.string.token_fail_text))
                    }
                } catch (e: Exception) {
                }
                Log.d("requestDataReceived2", e.toString())
            }
        }
    }

    private fun sendControlParamsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("requestData", "lifecycleScope start")
            val adapter = moshi.adapter(RequestAllParams::class.java).nonNull()
            val fastAdapter = moshi.adapter(RequestMonitorParams::class.java).nonNull()
            var requestControlJson = ""
            var requestMonitorJson = ""
            try {
                requestControlJson = adapter.toJson(RequestAllParams())
                requestMonitorJson = fastAdapter.toJson(RequestMonitorParams())
            } catch (e: Exception) {
                // toast("movie to JSON error = ${e.message}")
            }
            while (requestControlParams) {
                sendText(requestControlJson)
                Log.d("requestData", requestControlJson)
                delay(200)
                sendText(requestMonitorJson)
                delay(200)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun requestToken() {
        showAlertDialog(requireContext()) { password ->
            sendPassword(password)
            userPassword = password
            Log.d("requestDataReceived", password)
        }
    }

    private fun sendPassword(password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            controlViewModel.setWeldData(getPasswordRequestJsonString(password))
        }
    }

    private fun getPasswordRequestJsonString(password: String): String {
        val string = "{\"Write\":{\"Password\":$password}}"
        Log.d("requestData", string)
        return string

    }

    private fun savePassword(password: String) {
        val sharedPrefs =
            requireContext().getSharedPreferences(
                BtDeviceMonitorFragment.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )

        lifecycleScope.launch(Dispatchers.IO) {
            sharedPrefs.edit()
                .putString(Password.macAddress, password)
                .apply()
        }
    }
}

