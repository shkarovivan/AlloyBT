package com.example.alloybt.viewpager.device_monitor

import android.bluetooth.BluetoothDevice
import android.content.Context
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
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceInformation
import com.example.alloybt.R
import com.example.alloybt.control.ControlManager
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.livedata.state.ConnectionState

class BtDeviceMonitorFragment : Fragment(R.layout.fragment_device_control) {

    private var _binding: FragmentDeviceControlBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()

    private lateinit var btDevice: BluetoothDevice
    private lateinit var controlManager: ControlManager
    private var lastCurrent = 0
    private var lastTimeStamp: Long = 0

    private var requestMonitorData: Boolean = false
    private var isReady: Boolean = false

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

    }

    override fun onResume() {
        super.onResume()
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_MONITOR)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btDeviceInformation: BtDevice = BtDeviceInformation.btDeviceInformation

        btDevice = btDeviceInformation.device
        showConnectingBar()
        hideControlViews()

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Эллой " +
                btDeviceInformation.model + " №" + btDeviceInformation.seriesNumber

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
        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
//            if (btDataReceived.length in 1..3) {
//                voltageTextView.text = btDataReceived
//            }
            parseMonitorData(btDataReceived)
        }

        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestMonitorData = mode == MonitorMode.DEVICE_MONITOR
            if (requestMonitorData) {
                sendDataRequest()
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

            waveFormTextView.isVisible = false
            liftigTextView.isVisible = false
            weldTypeTextView.isVisible = false
            diamElectrodeTextView.isVisible = false
            torchModeTextView.isVisible = false
            weldOfImageView.isVisible = false
            weldOnImageView.isVisible = false
            wtOffImageView.isVisible = false
            wtOnImageView.isVisible = false
        }
    }

    private fun showControlViews() {
        with(binding) {
            btStateTextView.visibility = View.INVISIBLE
            stateProgressBar.visibility = View.INVISIBLE
            curTextView.visibility = View.VISIBLE
            currentHintTextView.visibility = View.VISIBLE

            waveFormTextView.isVisible = true
            liftigTextView.isVisible = true
            weldTypeTextView.isVisible = true
            diamElectrodeTextView.isVisible = true
            torchModeTextView.isVisible = true
            weldOfImageView.isVisible = true
            wtOffImageView.isVisible = true
        }

    }

    private fun parseMonitorData(data: String) {

        val moshi = Moshi.Builder().build()
        val weldParamsAdapter = moshi.adapter(WeldMonitorParams::class.java).nonNull()

        try {
            val weldParams = weldParamsAdapter.fromJson(data)
            if (weldParams != null) {
                showParams(weldParams)
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            //toast(e.toString())
        }
    }

    private fun showParams(params: WeldMonitorParams) {
        if (params.Response == "Ok") {
            with(binding) {
                curTextView.text = params.value.currentValue
                waveFormTextView.text = params.value.material
                liftigTextView.text = params.value.wireDiameter
                diamElectrodeTextView.text = params.value.gasType
                weldTypeTextView.text = params.value.weldType
                torchModeTextView.text = params.value.torchControl
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun sendDataRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (requestMonitorData) {
                if (isReady) {
                    sendText("")
                    delay(2000)
                    Log.d("requestData", "request")
                }
            }
        }
    }


}