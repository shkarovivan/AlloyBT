package com.example.alloybt.viewmodel

import com.example.alloybt.TigAllParams
import com.example.alloybt.json_data.TigMonitorParams
import com.example.alloybt.json_data.TigParamsList
import com.example.alloybt.json_data.TigValue

class ParamsRepository {

    private val tigValues: List<TigValue> = TigParamsList.tigParamsMap.values.toList()

    fun refreshMonitorParams(monitorParams: TigMonitorParams): List<TigValue> {
        with(monitorParams.value) {
            var newValue = TigParamsList.tigParamsMap["1000"]!!.copy(
                max = mode.toString(),
                value = when (mode) {
                    0 -> "AC"
                    1 -> "AC Pulse"
                    2 -> "DC"
                    3 -> "DC Pulse"
                    4 -> "AC+DC"
                    else -> "MMA"
                }
            )
            TigParamsList.tigParamsMap["1000"] = newValue

            // LiftTig
            newValue = TigParamsList.tigParamsMap["1001"]!!.copy(
                max = liftTig.toString(),
                value = when (liftTig) {
                    0 -> "Osc"
                    else -> "LiftTIg"
                }
            )
            TigParamsList.tigParamsMap["1001"] = newValue

            // WELD_BUTTON_MODE
            newValue = TigParamsList.tigParamsMap["1002"]!!.copy(
                max = weldButtonMode.toString(),
                value = when (weldButtonMode) {
                    0 -> "2T"
                    1 -> "4T"
                    2 -> "Точечный"
                    else -> "Повтор"
                }


            )
            TigParamsList.tigParamsMap["1002"] = newValue

            // waveForm
            newValue = TigParamsList.tigParamsMap["1003"]!!.copy(
                max = waveForm.toString(),
                value = when (waveForm) {
                    0 -> "Треугольная"
                    1 -> "Синус"
                    2 -> "прямоугольная"
                    else -> "Трапеция"
                }
            )
            TigParamsList.tigParamsMap["1003"] = newValue

            TigParamsList.tigParamsMap["1007"] =
                TigParamsList.tigParamsMap["1007"]!!.copy(value = workCurrent.toString())
            TigParamsList.tigParamsMap["101D"] =
                TigParamsList.tigParamsMap["101D"]!!.copy(value = diamElectrode.toString())

        }
        return TigParamsList.tigParamsMap.values.toList()
    }

    fun refreshAllParams(allParams: TigAllParams): List<TigValue> {

        with(allParams) {
            TigParamsList.tigParamsMap["1004"] =
                TigParamsList.tigParamsMap["1004"]!!.copy(value = timeGasStart.toString())
            TigParamsList.tigParamsMap["1005"] =
                TigParamsList.tigParamsMap["1005"]!!.copy(value = currentStart.toString())
            TigParamsList.tigParamsMap["1006"] =
                TigParamsList.tigParamsMap["1006"]!!.copy(value = timeToOperateCurrent.toString())
            TigParamsList.tigParamsMap["1009"] =
                TigParamsList.tigParamsMap["1009"]!!.copy(value = freqAC.toString())
            TigParamsList.tigParamsMap["100B"] =
                TigParamsList.tigParamsMap["100B"]!!.copy(value = percentAC.toString())
            TigParamsList.tigParamsMap["100D"] =
                TigParamsList.tigParamsMap["100F"]!!.copy(value = balanceAC.toString())
            TigParamsList.tigParamsMap["100F"] =
                TigParamsList.tigParamsMap["100D"]!!.copy(value = pulseCurrent1.toString())
            TigParamsList.tigParamsMap["1013"] =
                TigParamsList.tigParamsMap["1013"]!!.copy(value = freqPulse.toString())
            TigParamsList.tigParamsMap["1015"] =
                TigParamsList.tigParamsMap["1015"]!!.copy(value = currentPulse1.toString())
            TigParamsList.tigParamsMap["1017"] =
                TigParamsList.tigParamsMap["1017"]!!.copy(value = timeCurrentEnd.toString())
            TigParamsList.tigParamsMap["1019"] =
                TigParamsList.tigParamsMap["1019"]!!.copy(value = currentEnd.toString())
            TigParamsList.tigParamsMap["101B"] =
                TigParamsList.tigParamsMap["101B"]!!.copy(value = timeGasEnd.toString())
            TigParamsList.tigParamsMap["101F"] =
                TigParamsList.tigParamsMap["101F"]!!.copy(value = timeDot.toString())
            TigParamsList.tigParamsMap["1025"] =
                TigParamsList.tigParamsMap["1025"]!!.copy(value = mmaVoltageBreak.toString())
            TigParamsList.tigParamsMap["1027"] =
                TigParamsList.tigParamsMap["1027"]!!.copy(value = mmaArcForce.toString())
            TigParamsList.tigParamsMap["1029"] =
                TigParamsList.tigParamsMap["1029"]!!.copy(value = gasFlowRate.toInt().toString())

            val newValue = TigParamsList.tigParamsMap["102C"]!!.copy(
                value = when (remoteControl) {
                    0 -> "Выкл."
                    else -> "Вкл."
                }
            )
            TigParamsList.tigParamsMap["102C"] = newValue
        }


        return TigParamsList.tigParamsMap.values.toList()
    }

    fun initParamsList(): List<TigValue> {


        return TigParamsList.tigParamsMap.values.toList()
    }


}