package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class NextFolderProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : Processor, BaseEntitiesProcessor(yandexDisk, storagePageStrategy) {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.message()?.text()?.let { text ->
            val folderName = text.substring(1)
            session.currentPath = session.currentPath +
                    (if (session.currentPath.endsWith("/")) {
                        ""
                    } else "/") + "$folderName/"
            return getEntities(session, 0, 10)
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.startsWith("/") ?: false

    companion object {
        const val NAME = "file_tree_folder"
    }
}