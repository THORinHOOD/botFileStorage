package com.thorinhood.fileStorageBot.services.bot.pagination.yandex

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.ElementDescriptionStrategy
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import org.springframework.stereotype.Service

@Service
class YandexEntityDescriptionStrategy: ElementDescriptionStrategy<YandexEntity> {

    override fun description(index: Int, entity: YandexEntity): String =
        when (entity.type) {
            "file" -> "<b>/${index}.</b> [${entity.type}] <a href=\"${entity.href}\">${entity.name}</a>"
            "dir" -> "<b>/${index}.</b> [${entity.type}] ${entity.name}"
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