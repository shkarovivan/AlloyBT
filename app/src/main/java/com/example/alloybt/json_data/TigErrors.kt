package com.example.alloybt.json_data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TigErrors(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Page")
    val page: Int,
    @Json(name = "Value")
    val value: List<TigError>
)

@JsonClass(generateAdapter = true)
data class TigError(
    @Json(name = "N")
    val num: Int,
    @Json(name = "T")
    val time: Int,
    @Json(name = "L")
    val level: Int,
    @Json(name = "C")
    val code: Int
)

val tigErrorsDescriptions = mapOf(
    0 to "Питание включено",
    3 to "Аппарат заблокирован с панели",
    4 to "Аппарат заблокирован ключом 1",
    5 to "Аппарат заблокирован ключом 2",
    6 to "Аппарат заблокирован ключом 3",
    7 to "Аппарат заблокирован ключом 4",
    8 to "Аппарат заблокирован ключом 5",
    9 to "аппарат разблокирован с помощью пароля",
    10 to "Аппарат разлокирован ключом 1",
    11 to "Аппарат разлокирован ключом 2",
    12 to "Аппарат разлокирован ключом 3",
    13 to "Аппарат разлокирован ключом 4",
    14 to "Аппарат разлокирован ключом 5",
    15 to "перегрев аппарата",
    16 to "Ошибка БВО",
    17 to "Ошибка датчика контроля фаз",
    18 to "Ошибка потока газа",
    19 to "Ошибка давления газа",
)
