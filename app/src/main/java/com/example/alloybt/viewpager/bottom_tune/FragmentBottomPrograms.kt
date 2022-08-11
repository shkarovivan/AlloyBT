package com.example.alloybt.viewpager.bottom_tune


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.alloybt.BtDeviceInformation
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentDialogProgramsBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewpager.device_monitor.BtDeviceMonitorFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import kotlin.properties.Delegates


class FragmentBottomPrograms : BottomSheetDialogFragment() {

    private val controlViewModel: ControlViewModel by activityViewModels()

    private var _binding: FragmentDialogProgramsBinding? = null
    private val binding: FragmentDialogProgramsBinding
        get() = _binding!!


    private lateinit var dialog: BottomSheetDialog
    private var isActive = false
    private var lastTime = System.currentTimeMillis()
    private var onTime = 0L
    private var workDelay by Delegates.notNull<Int>()

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        workDelay = resources.getInteger(R.integer.bottom_sheet_delay_ms)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogProgramsBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BtDeviceMonitorFragment.bottomSheetIsEnabled = false
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        BtDeviceMonitorFragment.bottomSheetIsEnabled = true

        binding.button1.setOnClickListener {
            loadProgram(0)
            lastTime = System.currentTimeMillis()
        }
        binding.button1.setOnLongClickListener {
            saveProgram(0)
            lastTime = System.currentTimeMillis()
            return@setOnLongClickListener true
        }
        binding.button2.setOnClickListener {
            loadProgram(1)
            lastTime = System.currentTimeMillis()
        }
        binding.button2.setOnLongClickListener {
            saveProgram(1)
            lastTime = System.currentTimeMillis()
            return@setOnLongClickListener true
        }
        binding.button3.setOnClickListener {
            loadProgram(2)
            lastTime = System.currentTimeMillis()
        }
        binding.button3.setOnLongClickListener {
            saveProgram(2)
            lastTime = System.currentTimeMillis()
            return@setOnLongClickListener true
        }
        binding.button4.setOnClickListener {
            loadProgram(3)
            lastTime = System.currentTimeMillis()
        }
        binding.button4.setOnLongClickListener {
            saveProgram(3)
            lastTime = System.currentTimeMillis()
            return@setOnLongClickListener true
        }
        binding.button5.setOnClickListener {
            loadProgram(4)
            lastTime = System.currentTimeMillis()
        }
        binding.button5.setOnLongClickListener {
            saveProgram(4)
            lastTime = System.currentTimeMillis()
            return@setOnLongClickListener true
        }

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceived", btDataReceived)
            parseProgramsData(btDataReceived)

        }
        if (!isActive) {
            isActive = true
            sendValue()
        }
        checkWaitTime()
    }

    private fun parseProgramsData(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val response = jsonObject.getString("Response")
            if (response == "Programs") {
                val tigProgramList = emptyList<TigProgram>().toMutableList()
                val page = jsonObject.getInt("Page")
                if (page == 0) {
                    val valueArray = jsonObject.getJSONArray("Value")
                    (0 until valueArray.length()).map { index -> valueArray.getJSONObject(index) }
                        .map { movieJsonObject ->
                            val number = movieJsonObject.getInt("N")
                            val mode = movieJsonObject.getInt("M")
                            val current = movieJsonObject.getInt("I")
                            val buttonMode = movieJsonObject.getInt("B")
                            val program = TigProgram(number, mode, current, buttonMode)
                            tigProgramList += listOf(program)
                        }
                    tigProgramList.forEachIndexed { index, tigProgram ->
                       when (index){
                           0 -> binding.button1.text = "${tigProgramMode[tigProgram.mode]}\n${tigProgram.current} A\n${tigButtonMode[tigProgram.buttonMode]}"
                           1 -> binding.button2.text = "${tigProgramMode[tigProgram.mode]}\n${tigProgram.current} A\n${tigButtonMode[tigProgram.buttonMode]}"
                           2 -> binding.button3.text = "${tigProgramMode[tigProgram.mode]}\n${tigProgram.current} A\n${tigButtonMode[tigProgram.buttonMode]}"
                           3 -> binding.button4.text = "${tigProgramMode[tigProgram.mode]}\n${tigProgram.current} A\n${tigButtonMode[tigProgram.buttonMode]}"
                           4 -> binding.button5.text = "${tigProgramMode[tigProgram.mode]}\n${tigProgram.current} A\n${tigButtonMode[tigProgram.buttonMode]}"
                       }
                    }
                }
            }
        } catch (e: JSONException) {
            Log.d("requestDataReceivedError", "getJSONArray error - ${e.toString()}")
        }
    }

    private fun requestProgramsJsonString(): String {
        return "{\"Read\":\"Programs\",\"Page\":0}"
    }

    private fun loadProgramJsonString(number: Int): String {
        return "{\"Cmd\":{\"LoadProgram\":$number,\"Token\":${Password.token}}}"
    }

    private fun saveProgramJsonString(number: Int): String {
        return "{\"Cmd\":{\"SaveProgram\":$number,\"Token\":${Password.token}}}"
    }


    private fun requestFastPrograms() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val jsonString = requestProgramsJsonString()
                controlViewModel.setWeldData(jsonString)
                Log.d("writeData", jsonString)
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
        }
    }

    private fun checkWaitTime() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (onTime < workDelay) {
                onTime = System.currentTimeMillis() - lastTime
                delay(500)
            }
            this@FragmentBottomPrograms.dismiss()
        }
    }

    private fun sendValue() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (isActive) {
                requestFastPrograms()
                delay(1000)
            }
        }
    }

    private fun loadProgram(number: Int){
        lifecycleScope.launch(Dispatchers.IO) {
            (0..4).forEach { _ ->
                controlViewModel.setWeldData(loadProgramJsonString(number))
                delay(200)
            }
        }
    }

    private fun saveProgram(number: Int){
        lifecycleScope.launch(Dispatchers.IO) {
            (0..4).forEach { _ ->
                controlViewModel.setWeldData(saveProgramJsonString(number))
                delay(200)
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}