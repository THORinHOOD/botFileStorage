package com.geokittens.benchBot.services.bot.description

import com.thorinhood.benchLib.proto.bench.BenchInfo
import com.thorinhood.benchLib.proto.bench.BenchInfoWithDistance
import org.springframework.stereotype.Service

@Service
class BenchDescriptionStrategyImpl : BenchDescriptionStrategy {

    override fun description(benchInfo: BenchInfo): String {
        return propertiesDescription(benchInfo.propertiesMap)
    }

    override fun description(benchInfoWithDistance: BenchInfoWithDistance): String {
        var result = "Расстояние около ${benchInfoWithDistance.distance.toInt()} метров"
        result += propertiesDescription(benchInfoWithDistance.benchInfo.propertiesMap)
        return result
    }

    private fun propertiesDescription(properties: Map<String, String>): String {
        var result = ""
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

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }): String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }
}