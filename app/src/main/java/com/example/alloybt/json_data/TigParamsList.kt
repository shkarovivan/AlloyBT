package com.example.alloybt.json_data

object TigParamsList {

    var tigParamsMap = mutableMapOf(
        "1000" to TigValue(
            "1000",
            "Режим сварки",
            ParamType.ENUM,
            "AC,AC Pulse,DC,DC Pulse,AC+DC,MMA",
            "",
            ""
        ),
        "1001" to  TigValue("1001", "Поджиг", ParamType.ENUM, "OSCILLATOR,LiftTig", "", ""),
        "1002" to TigValue("1002", "Режим кнопки горелки", ParamType.ENUM, "2T,4T,SPOT, REPEAT", "", ""),
        "1003" to TigValue(
            "1003",
            "Форма волны (для режима AC)",
            ParamType.ENUM,
            "Треугольная,Синус,Прямоугольная,Трапецидальная",
            "",
            ""
        ),
        "1004" to TigValue("1004", "Время предпродувки", ParamType.FlOAT, "0", "", "0"),
        "1005" to TigValue("1005", "Стартовый ток", ParamType.FlOAT, "0", "", "0"),
        "1006" to TigValue(
            "1006",
            "Время перехода от стартового тока к рабочему",
            ParamType.FlOAT,
            "0",
            "",
            "0"
        ),
        "1007" to TigValue("1007", "Рабочий ток", ParamType.FlOAT, "0", "", "0"),
        "1009" to TigValue("1009", "Частота AC", ParamType.FlOAT, "0", "", "0"),
        "100B" to TigValue("100B", "Коэффициент AC", ParamType.FlOAT, "0", "", "0"),
        "100D" to TigValue("100D", "баланс AC", ParamType.FlOAT, "0", "", "0"),
        "100F" to TigValue("100F", "Пиковый ток импульса", ParamType.FlOAT, "0", "", "0"),
        "1011" to TigValue("1011", "Коэффициент импульса", ParamType.FlOAT, "0", "", "0"),
        "1013" to TigValue("1013", "Частота импульса", ParamType.FlOAT, "0", "", "0"),
        "1015" to TigValue("1015", "Базовый ток импульса", ParamType.FlOAT, "0", "", "0"),
        "1017" to TigValue("1017", "Время перезода к току заварки кратера", ParamType.FlOAT, "0", "", "0"),
        "1019" to TigValue("1019", "Ток заварки кратера", ParamType.FlOAT, "0", "", "0"),
        "101B" to TigValue("101B", "Время постпродувки", ParamType.FlOAT, "0", "", "0"),
        "101D" to TigValue("101D", "Диаметр электрода", ParamType.FlOAT, "0", "", "0"),
        "101F" to TigValue("101F", "Время точки", ParamType.FlOAT, "0", "", "0"),
        "1025" to TigValue("1025", "Напряжение обрыва дуги ММА", ParamType.FlOAT, "0", "", "0"),
        "1027" to TigValue("1027", "форсаж ММА", ParamType.FlOAT, "0", "", "0"),
        "1029" to TigValue("1029", "Расход газа", ParamType.FlOAT, "0", "", "0"),
        "102C" to TigValue("102C", "Управление от робота", ParamType.ENUM, "Выключено,Включено", "", "0"),
    )
}

data class TigValue(
    val address: String,
    val description: String,
    val type: ParamType,
    val min: String,
    val max: String = "",
    val value: String,

    )

enum class ParamType() {
    FlOAT,
    ENUM
}