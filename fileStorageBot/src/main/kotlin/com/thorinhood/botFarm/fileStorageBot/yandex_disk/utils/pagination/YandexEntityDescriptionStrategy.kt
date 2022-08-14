package com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination

import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import com.thorinhood.botFarm.engine.pagination.ElementDescriptionStrategy
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