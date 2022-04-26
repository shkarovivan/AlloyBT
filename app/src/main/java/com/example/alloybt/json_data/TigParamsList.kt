package com.example.alloybt.json_data

object TigParamsList {

    val tigParamsMap = listOf(
        TigValue(
            "1000",
            "Режим сварки",
            ParamType.ENUM,
            "AC,AC Pulse,DC,DC Pulse,AC+DC,MMA",
            "",
            0f
        ),
        TigValue("1001", "Поджиг", ParamType.ENUM, "OSCILLATOR,LiftTig", "", 0f),
        TigValue("1002", "Режим кнопки горелки", ParamType.ENUM, "2T,4T,SPOT, REPEAT", "", 0f),
        TigValue(
            "1003",
            "Форма волны",
            ParamType.ENUM,
            "Треугольная,Синус,Прямоугольная,Трапецидальная",
            "",
            0f
        ),
        TigValue("1004", "Время предпродувки", ParamType.FlOAT, "0", "", 0f),
        TigValue("1005", "Стартовый ток", ParamType.FlOAT, "0", "", 0f),
        TigValue(
            "1006",
            "Время перехода от стартового тока к рабочему",
            ParamType.FlOAT,
            "0",
            "",
            0f
        ),
        TigValue("1007", "Рабочий ток", ParamType.FlOAT, "0", "", 0f),
        TigValue("1009", "Частота AC", ParamType.FlOAT, "0", "", 0f),
        TigValue("100B", "Коэффициент AC", ParamType.FlOAT, "0", "", 0f),
        TigValue("100D", "баланс AC", ParamType.FlOAT, "0", "", 0f),
        TigValue("100F", "Пиковый ток импульса", ParamType.FlOAT, "0", "", 0f),
        TigValue("1011", "Коэффициент импульса", ParamType.FlOAT, "0", "", 0f),
        TigValue("1013", "Частота импульса", ParamType.FlOAT, "0", "", 0f),
        TigValue("1015", "Базовый ток импульса", ParamType.FlOAT, "0", "", 0f),
        TigValue("1017", "Время перезода к току заварки кратера", ParamType.FlOAT, "0", "", 0f),
        TigValue("1019", "Ток заварки кратера", ParamType.FlOAT, "0", "", 0f),
        TigValue("101B", "Время постпродувки", ParamType.FlOAT, "0", "", 0f),
        TigValue("101D", "Диаметр электрода", ParamType.FlOAT, "0", "", 0f),
        TigValue("101F", "Время точки", ParamType.FlOAT, "0", "", 0f),
        TigValue("1025", "Напряжение обрыва дуги ММА", ParamType.FlOAT, "0", "", 0f),
        TigValue("1027", "форсаж ММА", ParamType.FlOAT, "0", "", 0f),
        TigValue("1029", "Расход газа", ParamType.FlOAT, "0", "", 0f),
        TigValue("102C", "Управление от робота", ParamType.ENUM, "Выключено,Включено", "", 0f),
    )
}

data class TigValue(
    val address: String,
    val description: String,
    val type: ParamType,
    val min: String,
    val max: String = "",
    val value: Float,

    )

enum class ParamType() {
    FlOAT,
    ENUM
}