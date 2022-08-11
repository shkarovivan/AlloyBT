package com.example.alloybt.viewpager.bottom_tune

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentDialogTuneBinding
import com.example.alloybt.json_data.ParamType
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.material.bottomsheet.BottomSheetDialog


class FragmentBottomTune : BottomSheetDialogFragment() {

    private var _binding: FragmentDialogTuneBinding? = null
    private val binding: FragmentDialogTuneBinding
        get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()
    private var isActive = false
    private lateinit var dialog: BottomSheetDialog
    var newValue = ""
    var address = ""
    var koeff = 1
    var floatType = false
    private var lastTime = System.currentTimeMillis()
    private var onTime = 0L
    private val workDelay = 5000
    private val incrementPercent = 5

    val moshi: Moshi = Moshi.Builder().build()
    private val args: FragmentBottomTuneArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        Log.d("FastDialog", "onCreateDialog")
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogTuneBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BtDeviceMonitorFragment.bottomSheetIsEnabled = false
        _binding = null
    }

    @SuppressLint("LogNotTimber")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BtDeviceMonitorFragment.bottomSheetIsEnabled = true
        var tigValueMax = 0
        val tigValue = args.value
        address = tigValue.address
        binding.nameTextView.text = tigValue.description

        if (tigValue.type == ParamType.FLOAT) {
            floatType = true
        }
        if (floatType) {
            koeff = 10
        }
        if (tigValue.max.isNotEmpty()) {
            binding.tuneProgressBar.max =
                (tigValue.max.toFloat() * koeff).toInt()
            tigValueMax = binding.tuneProgressBar.max
        }
        if (tigValue.min.isNotEmpty()) binding.tuneProgressBar.min =
            (tigValue.min.toFloat() * koeff).toInt()
        binding.tuneProgressBar.progress = (tigValue.value.toFloat() * koeff).toInt()
        var lastProgress = binding.tuneProgressBar.progress
        val increment = tigValueMax * 5 / 100
        binding.valueTextView.text = tigValue.value
        Log.d(
            "tuneProgressBar",
            "${binding.tuneProgressBar.max}    ${binding.tuneProgressBar.min}  $koeff   ${tigValue.max} "
        )
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.tuneProgressBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, currentValue: Int, p2: Boolean) {
                var currentProgress = currentValue
                if (currentProgress - lastProgress > increment) {
                    currentProgress = lastProgress + increment
                    seekBar?.progress = currentProgress
                    lastProgress = currentProgress
                }
                if (currentProgress - lastProgress < -increment) {
                    currentProgress = lastProgress - increment
                    seekBar?.progress = currentProgress
                    lastProgress = currentProgress
                }

                newValue = if (floatType) {
                    (currentProgress.toFloat() / koeff).toString()
                } else {
                    (currentProgress / koeff).toString()
                }
                binding.valueTextView.text = newValue
                if (!isActive) {
                    isActive = true
                    sendValue()
                }
                lastTime = System.currentTimeMillis()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        )
        checkWaitTime()
    }

    private fun getJsonString(value: String, address: String): String {
        return "{\"Write\":{\"$address\":$value}}"
    }

    private fun sendWriteValueRequest(value: String, address: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val jsonString = getJsonString(value, address)
                controlViewModel.setWeldData(jsonString)
                Log.d("writeData", jsonString)
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
        }
    }

    private fun sendValue() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) {
                sendWriteValueRequest(newValue, address)
                delay(200)
            }
        }
    }

    private fun checkWaitTime() {
        lifecycleScope.launch(Dispatchers.Main) {
            while (onTime < workDelay) {
                onTime = System.currentTimeMillis() - lastTime
                delay(500)
            }
            this@FragmentBottomTune.dismiss()
        }
    }


    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}