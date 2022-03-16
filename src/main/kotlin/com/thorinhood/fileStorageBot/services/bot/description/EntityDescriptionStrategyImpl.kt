package com.thorinhood.fileStorageBot.services.bot.description

import com.thorinhood.fileStorageBot.data.Entity
import org.springframework.stereotype.Service

@Service
class EntityDescriptionStrategyImpl : EntityDescriptionStrategy {

    override fun description(index: Int, entityInfo: Entity): String =
        when (entityInfo.type) {
            "file" -> "<b>/${index}.</b> [${entityInfo.type}] <a href=\"${entityInfo.href}\">${entityInfo.name}</a>"
            "dir" -> "<b>/${index}.</b> [${entityInfo.type}] ${entityInfo.name}"
            else -> "[${entityInfo.type}] ${entityInfo.name}"
        }

    private fun orNone(caption: String, value: String?, converter: (String) -> String = { it }): String {
        return if (value != null) {
            "\n$caption : ${converter(value)}"
        } else {
            ""
        }
    }
}