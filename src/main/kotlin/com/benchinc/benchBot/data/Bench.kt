package com.benchinc.benchBot.data

import kotlin.math.roundToLong

data class Bench(val id: String,
                 val geometry: Point,
                 val properties: Map<String, String>,
                 val distance: Double) {

    override fun toString(): String {
        var result = "Расстояние около ${distance.roundToLong()} метров"
        result += orNone("спинка", properties["backrest"]) {
            when (it) {
                "yes" -> "да"
                "no" -> "нет"
                else -> "неизвестно"
            }
        }

        result += orNone("материал", properties["material"])
        result += orNone("поверхность", properties["surface"])
        result += orNone("цвет", properties["colour"])
        result += orNone("кол-во мест", properties["seats"])
        result += orNone("куда смотрит", properties["direction"])
        result += orNone("владелец", properties["operator"])
        result += orNone("надпись или посвящение", properties["inspiration"])
        return result
    }

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }) : String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }

}