package com.example.alloybt.viewpager.device_errors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.TigControlParams
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.databinding.FragmentErrorsListBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.skillbox.multithreading.adapters.ErrorsAdapter
import com.skillbox.networking.utils.autoCleared
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_errors_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ErrorsListFragment : Fragment(R.layout.fragment_errors_list) {

    private var _binding: FragmentErrorsListBinding? = null
    private val binding get() = _binding!!

    private var errorsAdapter: ErrorsAdapter by autoCleared()
    private val controlViewModel: ControlViewModel by activityViewModels()

    val moshi: Moshi = Moshi.Builder().build()

    private var requestErrorParams: Boolean = false
    private var isReady: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_ERRORS)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        // setExampleErrorList()

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceived2", btDataReceived)
            parseMonitorData(btDataReceived)

        }
        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestErrorParams = (mode == MonitorMode.DEVICE_ERRORS)
            if (requestErrorParams) {
                sendControlParamsRequest()
            }
        }
    }

    private fun initList() {
        errorsAdapter = ErrorsAdapter { position ->
            ///  call listener
        }

        with(binding.paramsListView) {
            adapter = errorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun parseMonitorData(data: String) {

        val tigErrorsAdapter = moshi.adapter(TigErrors::class.java).nonNull()

        try {
            val tigErrors = tigErrorsAdapter.fromJson(data)
            if (tigErrors != null) {
                errorsAdapter.items = tigErrors.value
            } else {
                toast("WeldParam = null")
            }

        } catch (e: Exception) {
            Log.d("requestDataReceived2", e.toString())
        }
    }

    private fun getJsonString(page: Int): String {
        return "{\"Read\":\"Errors\",\"Page\":$page}"
    }

    private fun sendControlParamsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (requestErrorParams) {
                (0..4).forEach {

                    val jsonString = getJsonString(it)
                    Log.d("requestDataReceived2","$jsonString")
                    controlViewModel.setWeldData(jsonString)
                    delay(200)
                }
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}