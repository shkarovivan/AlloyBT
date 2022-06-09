package com.example.alloybt.viewmodel

import com.example.alloybt.json_data.TigStatParams
import com.example.alloybt.json_data.*

class StatRepository {

    private val tigValues: List<TigValue> = TigParamsList.tigParamsMap.values.toList()

    fun refreshStatParams (statParams: TigStatParams): List<StatParam>{

        var newValue = TigStatList.tigStatMap["3000"]!!.copy(
                value = statParams.value.workTimeAll
            )
        TigStatList.tigStatMap["3000"] = newValue

        newValue = TigStatList.tigStatMap["3001"]!!.copy(
            value = statParams.value.weldTimeAll
        )
        TigStatList.tigStatMap["3001"] = newValue

        newValue = TigStatList.tigStatMap["3002"]!!.copy(
            value = statParams.value.workTimeLast
        )
        TigStatList.tigStatMap["3002"] = newValue

        newValue = TigStatList.tigStatMap["3002"]!!.copy(
            value = statParams.value.workTimeLast
        )
        TigStatList.tigStatMap["3002"] = newValue

        newValue = TigStatList.tigStatMap["3003"]!!.copy(
            value = statParams.value.weldTimeLast
        )
        TigStatList.tigStatMap["3003"] = newValue

        newValue = TigStatList.tigStatMap["3010"]!!.copy(
            value = statParams.value.bvoMotorFreq
        )
        TigStatList.tigStatMap["3010"] = newValue

        newValue = TigStatList.tigStatMap["3011"]!!.copy(
            value = statParams.value.bvoFlowSpeed
        )
        TigStatList.tigStatMap["3011"] = newValue

        newValue = TigStatList.tigStatMap["3012"]!!.copy(
            value = statParams.value.bvoTempBefore
        )
        TigStatList.tigStatMap["3012"] = newValue

        newValue = TigStatList.tigStatMap["3013"]!!.copy(
            value = statParams.value.bvoTempAfter
        )
        TigStatList.tigStatMap["3013"] = newValue

        newValue = TigStatList.tigStatMap["3014"]!!.copy(
            value = statParams.value.bvoTempMotor
        )
        TigStatList.tigStatMap["3014"] = newValue

        return TigStatList.tigStatMap.values.toList()
    }

    fun initStatList(): List<StatParam> {
        return TigStatList.tigStatMap.values.toList()
    }


}