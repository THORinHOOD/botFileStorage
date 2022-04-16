package com.thorinhood.fileStorageBot.bot.yandex_disk.utils.baseProcessors

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseEntitiesProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexConst
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexEntity
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

abstract class YandexBaseEntitiesProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy,
    name: String,
    procSpace: String = "",
) : BaseEntitiesProcessor<YandexEntity>(
    yandexDisk,
    yandexEntityPageStrategy,
    YandexConst.TOKEN,
    name,
    procSpace
) {

    override fun getContext(session: Session<Long>): PaginationContext<YandexEntity> =
        YandexUtils.getContext(session)

    override fun buildTransition(
        hasEntities: Boolean,
        extraArg: String
    ) = Transition(
        ProcSpaces.YANDEX_FILE_TREE, if (hasEntities) {
            "Объектов в папке [$extraArg] не найдено \uD83D\uDE1E"
        } else {
            "Переходим к объектам в папке [$extraArg]"
        }, KeyboardService.FILE_TREE_KEYBOARD)

}