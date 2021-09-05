package com.benchinc.benchBot.services.bot.helpers.strategies

import com.db.benchLib.data.bench.BenchDto
import com.db.benchLib.data.bench.BenchInfoWithDistance
import org.springframework.stereotype.Service

@Service
class BenchInfoStrategyImpl : BenchInfoStrategy {

    override fun description(benchDto: BenchDto): String {
        return propertiesDescription(benchDto.properties)
    }

    override fun description(benchInfoWithDistance: BenchInfoWithDistance): String {
        var result = "Расстояние около ${benchInfoWithDistance.distance.toInt()} метров"
        result += propertiesDescription(benchInfoWithDistance.properties)
        return result
    }

    private fun propertiesDescription(properties: Map<String, String>) : String {
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

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }) : String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }
}