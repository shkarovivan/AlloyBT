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
import com.example.alloybt.databinding.FragmentErrorsListBinding
import com.example.alloybt.json_data.*
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.skillbox.multithreading.adapters.ErrorsAdapter
import com.example.alloybt.utils.autoCleared
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class ErrorsListFragment : Fragment(R.layout.fragment_errors_list) {

    private var _binding: FragmentErrorsListBinding? = null
    private val binding get() = _binding!!

    private var errorsAdapter: ErrorsAdapter by autoCleared()
    private val controlViewModel: ControlViewModel by activityViewModels()

    val moshi: Moshi = Moshi.Builder().build()

    private var requestErrorParams: Boolean = false
    private var isReady: Boolean = false

    var maxPage = false
    var pageNumber = 0
    var list = emptyList<TigError>()

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
            Log.d("requestDataReceivedError", btDataReceived)
            parseErrorsData(btDataReceived)

        }
        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestErrorParams = (mode == MonitorMode.DEVICE_ERRORS)
            if (requestErrorParams) {
                sendErrorParamsRequest()
            }
        }
    }

    private fun initList() {
        errorsAdapter = ErrorsAdapter {
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

    private fun parseErrorsData(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val response = jsonObject.getString("Response")
            if (response == "Errors") {
                val tigErrorList = emptyList<TigError>().toMutableList()
                jsonObject.getInt("Page")
                val valueArray = jsonObject.getJSONArray("Value")
                (0 until valueArray.length()).map { index -> valueArray.getJSONObject(index) }
                    .map { movieJsonObject ->
                        val n = movieJsonObject.getInt("N").toInt()
                        val t = movieJsonObject.getInt("T").toInt()
                        val l = movieJsonObject.getInt("L").toInt()
                        val c = movieJsonObject.getInt("C").toInt()
                        val error = TigError(n, t, l, c)
                        tigErrorList += listOf(error)
                        Log.d("requestDataReceivedError", "TigError(n,t,l,c) - ${error.toString()}")
                    }
                if (tigErrorList.size < 5) maxPage = true
                list = errorsAdapter.items
                errorsAdapter.items = (list + tigErrorList).sortedBy { it.num }
               // errorsAdapter.items.plusAssign(tigErrorList)
//                if (tigErrorList.size<5)
//                if (page == 0 )
//                {list = emptyList()}
//               list += tigErrorList
//                if (page == 4 ) {
//                    list.sortedBy { it.num }
//                    errorsAdapter.
//
//                    errorsAdapter.items = list}
            }
        } catch (e: JSONException) {
            Log.d("requestDataReceivedError", "getJSONArray error - ${e.toString()}")
        }

//        val tigErrorsAdapter = moshi.adapter(TigErrors::class.java).nonNull()
//
//        try {
//            val tigErrors = tigErrorsAdapter.fromJson(data)
//            if (tigErrors != null) {
//                errorsAdapter.items = tigErrors.value
//                Log.d("requestDataReceivedError", tigErrors.toString())
//            } else {
//                toast("WeldParam = null")
//            }
//
//        } catch (e: Exception) {
//            Log.d("requestDataReceivedError", e.toString())
//        }
    }

    private fun getJsonString(page: Int): String {
        return "{\"Read\":\"Errors\",\"Page\":$page}"
    }

    private fun sendErrorParamsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (!maxPage && pageNumber < 20) {

                val jsonString = getJsonString(pageNumber)
                Log.d("requestDataReceivedError", "$jsonString")
                Log.d("requestDataReceivedError", "$pageNumber")
                Log.d("requestDataReceivedError", "$maxPage")
                controlViewModel.setWeldData(jsonString)
                pageNumber++
                delay(500)
            }
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}