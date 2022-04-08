package com.example.alloybt.viewpager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alloybt.viewpager.device_control.ControlParam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class ControlParamViewModel: ViewModel() {

    private val controlParamsLiveData = MutableLiveData<List<ControlParam>>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val controlParams: LiveData<List<ControlParam>>
        get() = controlParamsLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData


    fun getParams() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                controlParamsLiveData.postValue(getParamsList())
            }
        } catch (t: Throwable) {
            errorLiveData.postValue(t)
        }
    }


    //  перенести в репозиторий
    private fun getParamsList(): List<ControlParam> {
        val deviceParamsList = mutableListOf<ControlParam>()
        for (i in 0..7){
            val max = Random.nextInt(5, 30 )
            deviceParamsList +=  listOf(
                ControlParam(
                    title = Random.nextInt(1,1000).toString(),
                    description = listOf(
                        "Время стартового тока",
                        "Ток сварки",
                        "Напряжение сварки",
                        "Время заварки кратера",
                        "Ток заварки кратера",
                    ).random(),
                    type = "Float",
                    min = "0.0",
                    max = max.toString(),
                    value = Random.nextInt(0, max ).toString()
                ))
        }
        return deviceParamsList
    }
}