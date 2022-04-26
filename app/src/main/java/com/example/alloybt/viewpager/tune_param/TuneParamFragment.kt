package com.example.alloybt.viewpager.tune_param

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentTuneParamBinding
import com.example.alloybt.json_data.RequestMonitorParams
import com.example.alloybt.json_data.RequestWriteParams
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewpager.device_control.ControlParam
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TuneParamFragment : Fragment(R.layout.fragment_tune_param) {

    private var _binding: FragmentTuneParamBinding? = null
    private val binding get() = _binding!!

    private val controlViewModel: ControlViewModel by activityViewModels()

    private val args: TuneParamFragmentArgs by navArgs()
    private var lastTimeStamp = System.currentTimeMillis()
    private var newCurrent = 0
    private var isActive = false

    val moshi: Moshi = Moshi.Builder().build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTuneParamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isActive = false
        lifecycleScope.coroutineContext.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentValue = args.currentValue
        binding.croller.max = 500
        binding.croller.min = 0
        binding.croller.progress = currentValue

        binding.croller.setOnProgressChangedListener { value ->

            binding.paramValue.text = value.toString()
            newCurrent = value
            if (!isActive) {
                isActive = true
                sendCurrent()
            }
//            val now = System.currentTimeMillis()
//            if (now - lastTimeStamp > 100) {
//                if (lastCurrent != newCurrent) {
//                    sendWriteCurrentRequest(newCurrent)
//                    lastCurrent = newCurrent
//                    lastTimeStamp = System.currentTimeMillis()
//                }
//            }

        }


//        when (controlParam.type) {
//                 "Float" -> {
//                     binding.paramValue.text = ""
//                     binding.croller.progress = controlParam.value.toInt()
//                     binding.croller.min = (controlParam.min.toFloat() * 10).toInt()
//                     binding.croller.max = (controlParam.max.toFloat() * 10).toInt()
//                     binding.paramValue.text = (controlParam.value.toFloat() * 10).toString()
//                     binding.paramDescr.text = controlParam.description
//
//                     binding.croller.setOnProgressChangedListener { value ->
//                         binding.paramValue.text = (value / 10f).toString()
//                     }
//                 }
//        }
        binding.confirmButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun sendWriteCurrentRequest(current: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("requestData", "lifecycleScope start")
            val adapter = moshi.adapter(RequestWriteParams::class.java).nonNull()
            var requestWriteParamJson = ""
            var workCurrent = RequestWriteParams()
            workCurrent.value.value = current
            try {
                requestWriteParamJson = adapter.toJson(workCurrent)
                controlViewModel.setWeldData(requestWriteParamJson)
                Log.d("writeData", requestWriteParamJson)
            } catch (e: Exception) {
                toast("movie to JSON error = ${e.message}")
            }
        }
    }

     private fun sendCurrent(){
         lifecycleScope.launch(Dispatchers.IO) {
             while (isActive) {
                 sendWriteCurrentRequest(newCurrent)
                 delay(100)
             }
         }
     }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}