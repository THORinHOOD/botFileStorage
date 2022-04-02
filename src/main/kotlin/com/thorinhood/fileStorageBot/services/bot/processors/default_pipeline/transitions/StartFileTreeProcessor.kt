package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor

@Processor
class StartFileTreeProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : BaseEntitiesProcessor(
    yandexDisk,
    storagePageStrategy,
"startFileTree",
    "default"
) {

    override fun processInner(session: Session, update: Update): ProcessResult = getEntities(session)

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Список файлов"
    }
}