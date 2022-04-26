package com.example.alloybt.json_data

import com.squareup.moshi.Json

enum class TigErrors( mode: String ){
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


