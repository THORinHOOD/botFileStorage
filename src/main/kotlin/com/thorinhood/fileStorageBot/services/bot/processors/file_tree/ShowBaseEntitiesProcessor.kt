package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.yandex.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.YandexBaseEntitiesProcessor

@Processor
class ShowBaseEntitiesProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "showEntities",
    "file_tree"
) {

    override fun processInner(session: Session, update: Update): ProcessResult =
        getEntities(session)

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Показать объекты в текущей папке"
    }

}