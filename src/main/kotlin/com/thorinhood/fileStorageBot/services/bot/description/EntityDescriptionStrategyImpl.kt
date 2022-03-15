package com.thorinhood.fileStorageBot.services.bot.description

import com.thorinhood.fileStorageBot.data.Entity
import org.springframework.stereotype.Service

@Service
class EntityDescriptionStrategyImpl : EntityDescriptionStrategy {

    override fun description(entity: Entity): String =
        when (entity.type) {
            "file" -> "[${entity.type}] <a href=\"${entity.href}\">${entity.name}</a>"
            "dir" -> "[${entity.type}] /${entity.name}"
            else -> "[${entity.type}] ${entity.name}"
        }

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }): String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }
}