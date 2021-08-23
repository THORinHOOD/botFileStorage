package com.benchinc.benchBot.data

import de.westnordost.osmapi.map.data.Node
import kotlin.math.roundToLong

data class Bench(val node: Node,
                 val distance: Double) {

    override fun toString(): String {
        var result = "Расстояние около ${distance.roundToLong()} метров"
        result += orNone("спинка", node.tags["backrest"]) {
            when (it) {
                "yes" -> "да"
                "no" -> "нет"
                else -> "неизвестно"
            }
        }

        result += orNone("материал", node.tags["material"])
        result += orNone("поверхность", node.tags["surface"])
        result += orNone("цвет", node.tags["colour"])
        result += orNone("кол-во мест", node.tags["seats"])
        result += orNone("куда смотрит", node.tags["direction"])
        result += orNone("владелец", node.tags["operator"])
        result += orNone("надпись или посвящение", node.tags["inspiration"])
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