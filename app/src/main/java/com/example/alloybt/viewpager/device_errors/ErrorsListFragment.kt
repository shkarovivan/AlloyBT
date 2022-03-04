package com.example.alloybt.viewpager.device_errors

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alloybt.R
import com.skillbox.multithreading.adapters.ErrorsAdapter
import com.skillbox.networking.utils.autoCleared
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.fragment_errors_list.*


class ErrorsListFragment : Fragment(R.layout.fragment_errors_list) {

    private var errorsAdapter: ErrorsAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        setExampleErrorList()

    }

    private fun initList(){
        errorsAdapter = ErrorsAdapter { position ->
            ///  call listener
        }

        with(paramsListView) {
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
        val deviceError = DeviceError("Ошибка обмена информацией между платой источника питаняи и платой механизма подачи проволоки", time)
        val deviceErrorsList = mutableListOf<DeviceError>()
        for (i in 0..7){
            deviceErrorsList +=  listOf(deviceError.copy(time = time+ (minute+2*i).toString()))
        }

        errorsAdapter.items = deviceErrorsList
    }


}