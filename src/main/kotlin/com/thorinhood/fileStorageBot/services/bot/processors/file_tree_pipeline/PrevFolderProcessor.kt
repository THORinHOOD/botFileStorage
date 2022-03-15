package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class PrevFolderProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : Processor, BaseEntitiesProcessor(yandexDisk, storagePageStrategy) {

    override val name: String = NAME
    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val currentPath = session.currentPath
        if (currentPath.equals("disk:/")) {
            return listOf(SendMessage(session.chatId, "Вы уже в корневой папке").parseMode(ParseMode.HTML))
        }
        val lastSlash = currentPath.lastIndexOf("/")
        session.currentPath = currentPath.substring(0, lastSlash)
        return getEntities(session, 0, 10)
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.let {
             it.contains(NAME)
        } ?: false

    companion object {
        const val NAME = "Предыдущая папка"
    }
}