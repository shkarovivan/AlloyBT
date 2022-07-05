package com.example.alloybt.json_data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object TigStatList {

    var tigStatMap = mutableMapOf(
        "3000" to StatParam(
            "3000",
            "Время работы аппарата общее",
            StatType.TIME,
            "Часов",
            0
        ),
        "3001" to StatParam(
            "3001",
            "Время сварки общее",
            StatType.TIME,
            "Часов",
            0
        ),
        "3002" to StatParam(
            "3002",
            "Время работы с момента включения",
            StatType.TIME,
            "Часов",
            0
        ),
        "3003" to StatParam(
            "3003",
            "Время сварки с момента включения",
            StatType.TIME,
            "Часов",
            0
        ),
        "3010" to StatParam(
            "3010",
            "БВО-частота мотора",
            StatType.VALUE,
            "Герц",
            0
        ),
        "3011" to StatParam(
            "3011",
            "БВО-скорость потока жидкости",
            StatType.VALUE,
            "литров/минуту",
            0
        ),
        "3012" to StatParam(
            "3012",
            "БВО-температура жидкости до горелки",
            StatType.VALUE,
            "градусов",
            0
        ),
        "3013" to StatParam(
            "3013",
            "БВО-температура жидкости после горелки",
            StatType.VALUE,
            "градусов",
            0
        ),
        "3014" to StatParam(
            "3014",
            "БВО-температура мотора",
            StatType.VALUE,
            "градусов",
            0
        ),
    )
}

@Parcelize
data class StatParam(
    val address: String,
    val description: String,
    val type: StatType,
    val unit: String,
    var value: Number
    ) : Parcelable

enum class StatType() {
    TIME,
    VALUE,
}