package com.example.alloybt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alloybt.json_data.TigStatParams
import com.example.alloybt.json_data.StatParam

class StatViewModel: ViewModel() {

    private val repository = StatRepository()

    private val statLiveData = MutableLiveData<List<StatParam>>()

    val statParams: LiveData<List<StatParam>>
        get() = statLiveData

    fun initStatParamsList(){
        statLiveData.postValue(repository.initStatList())
    }

    fun refreshStatParams(statParams: TigStatParams){
        statLiveData.postValue(repository.refreshStatParams(statParams))
    }
}