package com.example.alloybt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alloybt.TigAllParams
import com.example.alloybt.json_data.TigMonitorParams
import com.example.alloybt.json_data.TigValue

class ParamsViewModel: ViewModel() {

    private val repository = ParamsRepository()

    private val paramsLiveData = MutableLiveData<List<TigValue>>()

    val params: LiveData<List<TigValue>>
        get() = paramsLiveData

    fun initParamsList(){
        paramsLiveData.postValue(repository.initParamsList())
    }

    fun refreshMonitorParams(monitorParams: TigMonitorParams){
        paramsLiveData.postValue(repository.refreshMonitorParams(monitorParams))
    }

    fun refreshAllParams(allParams: TigAllParams){
        paramsLiveData.postValue(repository.refreshAllParams(allParams))
    }
}