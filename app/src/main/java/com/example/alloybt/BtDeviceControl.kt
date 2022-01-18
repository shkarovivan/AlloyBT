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
import no.nordicsemi.android.ble.livedata.state.ConnectionState

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

        btDevice = btDeviceInformation.device
        showConnectingBar()
        hideControlViews()

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

        realCurrentTextView.visibility = View.VISIBLE
        controlViewModel.buttonState.observe(
            this
        ) { btDataReceived ->
            if (btDataReceived.length <= 3){
                realCurrentTextView.text = btDataReceived}
            }

        controlViewModel.connectionState.observe(this){
            when (it.state){
                ConnectionState.State.CONNECTING -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.connecting_state)}
                ConnectionState.State.INITIALIZING -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.initialization_state)}
                ConnectionState.State.READY -> {
                    showControlViews()
                    btStateTextView.text = ""
                }
                ConnectionState.State.DISCONNECTED -> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.disconnected_state)
                }
                ConnectionState.State.DISCONNECTING-> {
                    showConnectingBar()
                    hideControlViews()
                    btStateTextView.text = resources.getText(R.string.disconnecting_state)
                }
                else-> btStateTextView.visibility = View.INVISIBLE
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

    private fun sendText(current: String) {
        controlViewModel.setWeldCurrent(current)
    }

    private fun showConnectingBar(){
        btStateTextView.visibility = View.VISIBLE
        stateProgressBar.visibility = View.VISIBLE
    }

    private fun hideControlViews(){
        croller.visibility = View.INVISIBLE
        currentTextView.visibility = View.INVISIBLE
        currentHintTextView.visibility = View.INVISIBLE
        realCurrentTitleTextView.visibility = View.INVISIBLE
        realCurrentTextView.visibility = View.INVISIBLE
    }

    private fun showControlViews(){
        btStateTextView.visibility = View.INVISIBLE
        stateProgressBar.visibility = View.INVISIBLE
        croller.visibility = View.VISIBLE
        currentTextView.visibility = View.VISIBLE
        currentHintTextView.visibility = View.VISIBLE
        realCurrentTitleTextView.visibility = View.VISIBLE
        realCurrentTextView.visibility = View.VISIBLE
    }

}