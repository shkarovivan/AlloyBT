package com.example.alloybt

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_device_control.*

class BtDeviceControl : Fragment(R.layout.fragment_device_control) {

	private val args: BtDeviceControlArgs by navArgs()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val btDeviceInformation: BtDevice = args.btDeviceInformation

		modelTestTextView.text = btDeviceInformation.model
		macAdressTestTextView.text = btDeviceInformation.macAddress
		seriesNumberTestTextView.text = btDeviceInformation.seriesNumber

	}
}