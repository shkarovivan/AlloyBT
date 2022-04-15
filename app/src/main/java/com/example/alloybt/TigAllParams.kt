package com.example.alloybt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TigVersions(
    @Json(name = "0001")
    val protocolVersion: String,
    @Json(name = "0002")
    val mainBoardVersion: String,
    @Json(name = "0003")
    val tftBoardVersion: String,
    @Json(name = "0004")
    val tftBoardDate: String,
)

@JsonClass(generateAdapter = true)
data class TigAllParams(
    @Json(name = "1000")
    val mode: TigMode,
    @Json(name = "1001")
    val liftTig: LiftTig,
    @Json(name = "1002")
    val weldButtonMode: WeldButtonMode,
    @Json(name = "1003")
    val waveForm: WaveForm,
    @Json(name = "1004")
    val timeGasStart: Float,
    @Json(name = "1005")
    val currentStart: Float,
    @Json(name = "1006")
    val timeToOperateCurrent: Float,
    @Json(name = "1007")
    val workCurrent: Float,
    @Json(name = "1009")
    val freqAC: Float,
    @Json(name = "100B")
    val percentAC: Float,
    @Json(name = "100D")
    val balanceAC: Float,
    @Json(name = "100F")
    val pulseCurrent1: Float,
    @Json(name = "1011")
    val pulseCurrent: Float,
    @Json(name = "1013")
    val freqPulse: Float,
    @Json(name = "1015")
    val currentPulse1: Float,
    @Json(name = "1017")
    val timeCurrentEnd: Float,
    @Json(name = "1019")
    val currentEnd: Float,
    @Json(name = "101B")
    val timeGasEnd: Float,
    @Json(name = "101D")
    val diamElectrode: Float,
    @Json(name = "101F")
    val timeDot: Float,
    @Json(name = "1025")
    val mmaVoltageBreak: Float,
    @Json(name = "1027")
    val mmaArcForce: Float,
    @Json(name = "1029")
    val gasFlowRate: Float,
    @Json(name = "102С")
    val remoteControl: Float,
    @Json(name = "2000")
    val realCurrent: Float,
    @Json(name = "2002")
    val realVoltage: Float,
    @Json(name = "2004")
    val state: State,
    @Json(name = "2005")
    val errors: Errors,
)

@JsonClass(generateAdapter = true)
data class TigFastParams(
    @Json(name = "1000")
    val mode: TigMode,
    @Json(name = "1001")
    val liftTig: LiftTig,
    @Json(name = "1002")
    val weldButtonMode: WeldButtonMode,
    @Json(name = "1003")
    val waveForm: WaveForm,
    @Json(name = "1007")
    val workCurrent: Float,
    @Json(name = "101D")
    val diamElectrode: Float ,
    @Json(name = "2000")
    val realCurrent: Float,
    @Json(name = "2002")
    val realVoltage: Float,
    @Json(name = "2004")
    val state: State,
    @Json(name = "2005")
    val errors: Errors,
    )

enum class TigMode(mode: String) {
    @Json(name = "0")
    AC("AC"),
    @Json(name = "1")
    AC_PULSE("AC Pulse"),
    @Json(name = "2")
    DC("DC"),
    @Json(name = "3")
    DC_PULSE("DC Pulse"),
    @Json(name = "4")
    AC_DC("AC+DC"),
    @Json(name = "5")
    MMA("MMA")
}

enum class LiftTig(mode: String) {
    @Json(name = "0")
    OSC("OSC"),
    @Json(name = "1")
    LIFT_TIG("LiftTIG")
}

enum class WeldButtonMode(mode: String) {
    @Json(name = "0")
    M2T("2T"),
    @Json(name = "1")
    M4T("4T"),
    @Json(name = "2")
    SPOT("SPOT"),
    @Json(name = "3")
    REPEAT("REPEAT")
}

enum class WaveForm(mode: String) {
    @Json(name = "0")
    TRIANGLE("Triangle"),
    @Json(name = "1")
    SINUS("SINUS"),
    @Json(name = "2")
    MEANDER("MEANDER"),
    @Json(name = "3")
    TRAPEZOID("TRAPEZOID")
}

enum class RemoteControl( mode: String ){
    @Json(name = "0")
    OFF("Robot OFF"),
    @Json(name = "1")
    On("Robot ON"),
}

enum class State( mode: String ){
    @Json(name = "0")
    BUTTON_OFF("Button OFF"),
    @Json(name = "1")
    BUTTON_ON("Robot ON"),
    @Json(name = "2")
    ARC_ON("Arc ON"),
}

enum class Errors( mode: String ){
    @Json(name = "0")
    NO_ERRORS(""),
    @Json(name = "1")
    OVERHEAT("Перегрев"),
    @Json(name = "2")
    WATER_FLOW("Ошибка БВО"),
    @Json(name = "3")
    PHASE_CONTROL("Ошибка контроля фаз"),
    @Json(name = "4")
    GAS_FLOW("Ошибка потока газа"),
    @Json(name = "5")
    GAS_PRESSURE("Ошибка давления газа"),
}



