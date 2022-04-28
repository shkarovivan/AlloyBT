package com.example.alloybt.viewpager.device_monitor

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceInformation
import com.example.alloybt.R
import com.example.alloybt.control.ControlManager
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.example.alloybt.viewmodel.ParamsViewModel
import com.example.alloybt.viewpager.ViewPagerFragmentDirections
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import no.nordicsemi.android.ble.livedata.state.ConnectionState

class BtDeviceMonitorFragment : Fragment(R.layout.fragment_device_control) {

    private var _binding: FragmentDeviceControlBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()
    private val paramsViewModel: ParamsViewModel by activityViewModels()

    private lateinit var btDevice: BluetoothDevice
    private lateinit var controlManager: ControlManager

    private var requestMonitorData: Boolean = false
    private var isReady: Boolean = false
    private var isPause: Boolean = true

    var current = 0

    val moshi: Moshi = Moshi.Builder().build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        lifecycleScope.coroutineContext.cancel()
    }

    override fun onResume() {
        super.onResume()
        isPause = false
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_MONITOR)
        Log.d("requestData", "onResume")
    }


    override fun onPause() {
        super.onPause()
        isPause = true
        Log.d("requestData", "onPause")
        //  lifecycleScope.coroutineContext.cancel()
        lifecycleScope.coroutineContext.cancelChildren()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btDeviceInformation: BtDevice = BtDeviceInformation.btDeviceInformation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        btDevice = btDeviceInformation.device
        showConnectingBar()
        hideControlViews()
        isPause = false

        (activity as AppCompatActivity?)!!.supportActionBar!!.title =
            btDeviceInformation.model// + " № " + btDeviceInformation.seriesNumber

//        binding.croller.setOnProgressChangedListener { current ->
//            val now = System.currentTimeMillis()
//            if (now - lastTimeStamp > 50) {
//                if (lastCurrent != current) {
//                   // currentTextView.text = current.toString()
//                    sendText(current.toString())
//                    lastCurrent = current
//                    lastTimeStamp = System.currentTimeMillis()
//                }
//            }
//        }

        binding.curTextView.setOnClickListener {

//            val action = ViewPagerFragmentDirections .actionViewPagerFragmentToTuneParamFragment(current)
//            findNavController().navigate(action)

            val currentValue = TigParamsList.tigParamsMap["1007"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomTune(currentValue!!)
            findNavController().navigate(action)
        }

        binding.weldTypeTextView.setOnClickListener {
            val tigValue = TigParamsList.tigParamsMap["1000"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
            findNavController().navigate(action)
        }

        binding.waveFormImageView.setOnClickListener {
            val tigValue = TigParamsList.tigParamsMap["1003"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
            findNavController().navigate(action)
        }

        binding.diamElectrodeTextView.setOnClickListener {
            val diamValue = TigParamsList.tigParamsMap["101D"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomTune(diamValue!!)
            findNavController().navigate(action)
        }

        binding.torchModeTextView.setOnClickListener {
            val tigValue = TigParamsList.tigParamsMap["1002"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
            findNavController().navigate(action)
        }

        binding.lifTigImageView.setOnClickListener {
            val tigValue = TigParamsList.tigParamsMap["1001"]
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToFragmentBottomEnum(tigValue!!)
            findNavController().navigate(action)
        }


        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceived", btDataReceived)
            parseMonitorData(btDataReceived)

        }

        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestMonitorData = (mode == MonitorMode.DEVICE_MONITOR)
            if (requestMonitorData) {
                sendMonitorRequest()
            }
        }

        controlViewModel.connectionState.observe(viewLifecycleOwner) {
            when (it.state) {
                ConnectionState.State.CONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.connecting_state)
                    isReady = false

                }
                ConnectionState.State.INITIALIZING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.initialization_state)
                    isReady = false
                }
                ConnectionState.State.READY -> {
                    showControlViews()
                    binding.btStateTextView.text = ""
                    isReady = true
                    controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_MONITOR)
                }
                ConnectionState.State.DISCONNECTED -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.disconnected_state)
                    isReady = false
                }
                ConnectionState.State.DISCONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    binding.btStateTextView.text = resources.getText(R.string.disconnecting_state)
                    isReady = false
                }
                else -> binding.btStateTextView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        controlManager = ControlManager(context)
    }

    override fun onStart() {
        super.onStart()
        controlViewModel.connect(btDevice)
    }

    private fun sendText(data: String) {
        controlViewModel.setWeldData(data)
    }

    private fun showConnectingBar() {
        binding.btStateTextView.visibility = View.VISIBLE
        binding.stateProgressBar.visibility = View.VISIBLE
    }

    private fun hideControlViews() {
        with(binding) {
            // croller.visibility = View.INVISIBLE
            curTextView.visibility = View.INVISIBLE
            currentHintTextView.visibility = View.INVISIBLE
            upPanelImageView.visibility= View.INVISIBLE
            waveFormImageView.isVisible = false
            lifTigImageView.isVisible = false
            weldTypeTextView.isVisible = false
            diamElectrodeTextView.isVisible = false
            torchModeTextView.isVisible = false
            weldOfImageView.isVisible = false
            weldOnImageView.isVisible = false
            wtOffImageView.isVisible = false
            wtOnImageView.isVisible = false
            lockClosedImageView.isVisible = false
            lockOpenImageView.isVisible = false
        }
    }

    private fun showControlViews() {
        with(binding) {
            btStateTextView.visibility = View.INVISIBLE
            stateProgressBar.visibility = View.INVISIBLE
            curTextView.visibility = View.VISIBLE
            currentHintTextView.visibility = View.VISIBLE
            upPanelImageView.visibility= View.VISIBLE

            waveFormImageView.isVisible = true
            lifTigImageView.isVisible = true
            weldTypeTextView.isVisible = true
            diamElectrodeTextView.isVisible = true
            torchModeTextView.isVisible = true
            weldOfImageView.isVisible = true
            wtOffImageView.isVisible = false
            lockClosedImageView.isVisible = false
            lockOpenImageView.isVisible = false
        }
    }

    private fun parseMonitorData(data: String) {
        val moshi = Moshi.Builder().build()
        val tigFastParamsAdapter = moshi.adapter(TigMonitorParams::class.java).nonNull()

        try {
            val tigFastParams = tigFastParamsAdapter.fromJson(data)
            if (tigFastParams != null) {
                Log.d("requestData", "fromJSON Ok")
                showParams(tigFastParams)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            Log.d("requestData", e.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showParams(params: TigMonitorParams) {

        with(binding) {
            Log.d("requestData", params.toString())
            if (params.value.state == 2) {
                curTextView.text = params.value.realCurrent.toInt().toString()
            } else
            {curTextView.text = params.value.workCurrent.toString()
                current = params.value.workCurrent
            }

            waveFormImageView.setImageLevel(params.value.waveForm)
            lifTigImageView.setImageLevel(params.value.liftTig)
            diamElectrodeTextView.text = params.value.diamElectrode.toString() + "мм"
            weldTypeTextView.text =
                when (params.value.mode) {
                    4 -> "AC+DC"
                    0 -> "AC"
                    1 -> "AC Pulse"
                    3 -> "DC Pulse"
                    5 -> "MMA"
                    2 -> "DC"
                    else-> "Err"
                }

            torchModeTextView.text =
                when(params.value.weldButtonMode){
                    0 -> "2T"
                    1 -> "4T"
                    2 -> "Spot"
                    3 -> "Repeat"
                    else-> "Err"
                }
        }
        paramsViewModel.refreshMonitorParams(params)
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun sendMonitorRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("requestData", "lifecycleScope start")
            val adapter = moshi.adapter(RequestMonitorParams::class.java).nonNull()
            var requestMonitorJson = ""
            try {
                requestMonitorJson = adapter.toJson(RequestMonitorParams())
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
            while (requestMonitorData && isReady/*&& !isPause*/) {
                sendText(requestMonitorJson)
                Log.d("requestData", requestMonitorJson)
                delay(100)
            }
        }
    }
}