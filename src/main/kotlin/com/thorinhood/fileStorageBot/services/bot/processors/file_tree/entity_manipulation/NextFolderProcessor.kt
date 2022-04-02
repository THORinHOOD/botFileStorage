package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor

@Processor
class NextFolderProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : BaseEntitiesProcessor(
    yandexDisk,
    storagePageStrategy,
    "nextFolder",
    "file_tree#entity_manipulation"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val folderName = session.fileTreeInfo.indexToEntity[session.cursor.args["entity"]]?.name ?:
            return ProcessResult.EMPTY_RESULT
        session.fileTreeInfo.currentPath = session.fileTreeInfo.currentPath +
                (if (session.fileTreeInfo.currentPath.endsWith("/")) {
                    ""
                } else "/") + "$folderName/"
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Перейти в папку"
    }
}