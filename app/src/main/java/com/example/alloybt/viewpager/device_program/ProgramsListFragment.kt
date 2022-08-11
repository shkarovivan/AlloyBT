package com.example.alloybt.viewpager.device_program

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
import com.example.alloybt.databinding.FragmentProgramsListBinding
import com.example.alloybt.json_data.Password
import com.example.alloybt.json_data.TigError
import com.example.alloybt.json_data.TigProgram
import com.example.alloybt.utils.autoCleared
import com.example.alloybt.viewmodel.ControlViewModel
import com.example.alloybt.viewmodel.MonitorMode
import com.squareup.moshi.Moshi
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class ProgramsListFragment : Fragment(R.layout.fragment_programs_list) {

    private var _binding: FragmentProgramsListBinding? = null
    private val binding get() = _binding!!

    private var programsAdapter: DeviceProgramAdapter by autoCleared()
    private val controlViewModel: ControlViewModel by activityViewModels()

    val moshi: Moshi = Moshi.Builder().build()

    private var requestPrograms: Boolean = false
    private var isReady: Boolean = false

    var maxPage = false
    var pageNumber = 0
    var list = emptyList<TigProgram>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        controlViewModel.monitorMode.postValue(MonitorMode.DEVICE_PROGRAM)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()

        controlViewModel.dataFromBtDevice.observe(
            viewLifecycleOwner
        ) { btDataReceived ->
            Log.d("requestDataReceivedError", btDataReceived)
            parseProgramData(btDataReceived)

        }
        controlViewModel.monitorMode.observe(viewLifecycleOwner) { mode ->
            requestPrograms = (mode == MonitorMode.DEVICE_PROGRAM)
            if (requestPrograms) {
                sendProgramsRequest()
            }
        }
    }

    private fun initList() {
        programsAdapter = DeviceProgramAdapter({ position ->
            if (Password.token == null) {
                toast(resources.getString(R.string.no_password_entered))
            } else {
                saveProgram(position)
            }
        },
            { position ->
                if (Password.token == null) {
                    toast(resources.getString(R.string.no_password_entered))
                } else {
                    loadProgram(position)
                }
            })

        with(binding.programsListView) {
            adapter = programsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
    }

    private fun parseProgramData(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val response = jsonObject.getString("Response")
            if (response == "Programs") {
                val tigProgramList = emptyList<TigProgram>().toMutableList()
                val page = jsonObject.getInt("Page")
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



                list = programsAdapter.items
                programsAdapter.items = (list + tigProgramList).sortedBy { it.number }

            }
        } catch (e: JSONException) {
            Log.d("requestDataReceivedError", "getJSONArray error - ${e.toString()}")
        }
    }

    private fun requestProgramsJsonString(page: Int): String {
        return "{\"Read\":\"Programs\",\"Page\":$page}"
    }

    private fun loadProgramJsonString(number: Int): String {
        return "{\"Cmd\":{\"LoadProgram\":$number,\"Token\":${Password.token}}}"
    }

    private fun saveProgramJsonString(number: Int): String {
        return "{\"Cmd\":{\"SaveProgram\":$number,\"Token\":${Password.token}}}"
    }

    private fun sendProgramsRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (pageNumber < 20) {
                val jsonString = requestProgramsJsonString(pageNumber)
                controlViewModel.setWeldData(jsonString)
                pageNumber++
                delay(500)
            }
        }
    }

    private fun loadProgram(number: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            (0..4).forEach { _ ->
                controlViewModel.setWeldData(loadProgramJsonString(number))
                delay(200)
            }
        }
    }

    private fun saveProgram(number: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
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