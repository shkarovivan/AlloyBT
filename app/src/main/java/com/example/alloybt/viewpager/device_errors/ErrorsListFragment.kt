package com.example.alloybt.viewpager.device_errors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.example.alloybt.databinding.FragmentDeviceControlBinding
import com.example.alloybt.databinding.FragmentErrorsListBinding
import com.skillbox.multithreading.adapters.ErrorsAdapter
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_errors_list.*


class ErrorsListFragment : Fragment(R.layout.fragment_errors_list) {

    private var _binding: FragmentErrorsListBinding? = null
    private val binding get() = _binding!!

    private var errorsAdapter: ErrorsAdapter by autoCleared()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        setExampleErrorList()
    }

    private fun initList(){
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

    private fun setExampleErrorList(){
        val minute = 12
        val time = "2022:03:13 14:"
        val deviceError = DeviceError("Ошибка обмена информацией между платой источника питания и платой механизма подачи проволоки", time)
        val deviceErrorsList = mutableListOf<DeviceError>()
        for (i in 0..7){
            deviceErrorsList +=  listOf(deviceError.copy(time = time+ (minute+2*i).toString()))
        }

        errorsAdapter.items = deviceErrorsList
    }


}