package com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseEntitiesProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.pagination.yandex.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.services.bot.yandex.YandexUtils

abstract class YandexBaseEntitiesProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy,
    name: String,
    procSpace: String = "",
) : BaseEntitiesProcessor<YandexEntity>(
    yandexDisk,
    yandexEntityPageStrategy,
    "yandex_token",
    name,
    procSpace
) {

    override fun getContext(session: Session): PaginationContext<YandexEntity> =
        YandexUtils.getContext(session)

    override fun buildTransition(
        hasEntities: Boolean,
        extraArg: String
    ) = Transition("file_tree", if (hasEntities) {
            "Объектов в папке [$extraArg] не найдено \uD83D\uDE1E"
        } else {
            "Переходим к объектам в папке [$extraArg]"
        }, KeyboardService.FILE_TREE_KEYBOARD)

}