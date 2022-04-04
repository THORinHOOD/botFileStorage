package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.data.FileTreeInfo
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor

@Processor
class PrevFolderProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : BaseEntitiesProcessor(
    yandexDisk,
    storagePageStrategy,
    "prevFolder",
    "file_tree"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val currentPath = (session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath
        if (currentPath == "disk:/") {
            return ProcessResult(listOf(SendMessage(session.chatId, "Вы уже в корневой папке").parseMode(ParseMode.HTML)))
        }
        val s = currentPath.substring(0, currentPath.lastIndexOf("/"))
        (session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath = s.substring(0, s.lastIndexOf("/") + 1)
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Предыдущая папка"
    }
}