package com.example.alloybt

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.alloybt.control.ControlManager
import com.example.alloybt.viewmodel.ControlViewModel
import kotlinx.android.synthetic.main.fragment_device_control.*

class BtDeviceControl : Fragment(R.layout.fragment_device_control) {

    private val args: BtDeviceControlArgs by navArgs()

    private val controlViewModel: ControlViewModel by viewModels()

    private lateinit var btDevice: BluetoothDevice
    private lateinit var controlManager: ControlManager
    private var lastCurrent = 0
    private var lastTimeStamp: Long = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btDeviceInformation: BtDevice = args.btDeviceInformation

        modelTestTextView.text = btDeviceInformation.model
        macAdressTestTextView.text = btDeviceInformation.macAddress
        seriesNumberTestTextView.text = btDeviceInformation.seriesNumber
        btDevice = btDeviceInformation.device

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Эллой "+
            btDeviceInformation.model + " №" + btDeviceInformation.seriesNumber
        croller.setOnProgressChangedListener { current ->
            val now = System.currentTimeMillis()
            if (now - lastTimeStamp > 50) {
                if (lastCurrent != current) {
                    currentTextView.text = current.toString()
                    sendText(current.toString())
                    lastCurrent = current
                    lastTimeStamp = System.currentTimeMillis()
                }
            }
        }
        readTextView.visibility = View.VISIBLE
        controlViewModel.buttonState.observe(
            this
        ) { btDataReceived ->
            if (btDataReceived.length <= 3){
                readTextView.text = btDataReceived}
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

    private fun sendText(current: String) {
        controlViewModel.setWeldCurrent(current)
    }
}