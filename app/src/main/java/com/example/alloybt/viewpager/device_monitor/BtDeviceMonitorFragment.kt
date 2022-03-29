package com.example.alloybt.viewpager.device_monitor

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.alloybt.BtDevice
import com.example.alloybt.BtDeviceInformation
import com.example.alloybt.R
import com.example.alloybt.control.ControlManager
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewpager.AddBadge
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.fragment_device_control.*
import no.nordicsemi.android.ble.livedata.state.ConnectionState

class BtDeviceMonitorFragment() : Fragment(R.layout.fragment_device_control)  {

    private val controlViewModel: ControlViewModel by activityViewModels()

    private lateinit var btDevice: BluetoothDevice
    private lateinit var controlManager: ControlManager
    private var lastCurrent = 0
    private var lastTimeStamp: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btDeviceInformation: BtDevice = BtDeviceInformation.btDeviceInformation

        btDevice = btDeviceInformation.device
        showConnectingBar()
        hideControlViews()

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Эллой " +
                btDeviceInformation.model + " №" + btDeviceInformation.seriesNumber

        croller.setOnProgressChangedListener { current ->
            val now = System.currentTimeMillis()
            if (now - lastTimeStamp > 50) {
                if (lastCurrent != current) {
                   // currentTextView.text = current.toString()
                    sendText(current.toString())
                    lastCurrent = current
                    lastTimeStamp = System.currentTimeMillis()
                }
            }
        }

        voltageTextView.visibility = View.VISIBLE
        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
//            if (btDataReceived.length in 1..3) {
//                voltageTextView.text = btDataReceived
//            }
            parseMonitorData(btDataReceived)
        }

        controlViewModel.connectionState.observe(viewLifecycleOwner) {
            when (it.state) {
                ConnectionState.State.CONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.connecting_state)
                }
                ConnectionState.State.INITIALIZING -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.initialization_state)
                }
                ConnectionState.State.READY -> {
                    showControlViews()
                    btStateTextView.text = ""
                }
                ConnectionState.State.DISCONNECTED -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.disconnected_state)
                }
                ConnectionState.State.DISCONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.disconnecting_state)
                }
                else -> btStateTextView.visibility = View.INVISIBLE
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
        btStateTextView.visibility = View.VISIBLE
        stateProgressBar.visibility = View.VISIBLE
    }

    private fun hideControlViews() {
        croller.visibility = View.INVISIBLE
        curTextView.visibility = View.INVISIBLE
        currentHintTextView.visibility = View.INVISIBLE
        voltageHintTextView.visibility = View.INVISIBLE
        voltageTextView.isVisible = false

        gasTextView.isVisible = false
        wireDiaTextView.isVisible = false
        weldTypeTextView.isVisible = false
        materialTextView.isVisible = false
        torchTextView.isVisible = false
        weldOfImageView.isVisible = false
        weldOnImageView.isVisible = false
        wtOffImageView.isVisible = false
        wtOnImageView.isVisible = false

    }

    private fun showControlViews() {
        btStateTextView.visibility = View.INVISIBLE
        stateProgressBar.visibility = View.INVISIBLE
        curTextView.visibility = View.VISIBLE
        currentHintTextView.visibility = View.VISIBLE
        voltageHintTextView.visibility = View.VISIBLE

        voltageTextView.isVisible = true
        gasTextView.isVisible = true
        wireDiaTextView.isVisible = true
        weldTypeTextView.isVisible = true
        materialTextView.isVisible = true
        torchTextView.isVisible = true
        weldOfImageView.isVisible = true
        wtOffImageView.isVisible = true

    }

    private fun parseMonitorData(data:String){

        val moshi = Moshi.Builder().build()
        val weldParamsAdapter = moshi.adapter(WeldMonitorParams::class.java).nonNull()

        try {
            val weldParams = weldParamsAdapter.fromJson(data)
            if (weldParams != null) {
                showParams(weldParams)
            }
            else {
                toast ("WeldParam = null")
            }

        } catch (e: Exception) {
           toast(e.toString())
        }
    }

    private fun showParams(params: WeldMonitorParams){
        if (params.Response == "Ok") {
            curTextView.text = params.value.currentValue
            voltageTextView.text = params.value.voltageValue
            materialTextView.text = params.value.material
            wireDiaTextView.text = params.value.wireDiameter
            gasTextView.text = params.value.gasType
            weldTypeTextView.text = params.value.weldType
            torchTextView.text = params.value.torchControl
        }
    }

    private fun toast(text: String){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show()
    }

}