package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor

@Processor
class PrevFolderProcessorBase(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "prevFolder",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val currentPath = YandexUtils.getCurrentPath(session)
        if (currentPath == "disk:/") {
            return ProcessResult(listOf(SendMessage(session.chatId, "Вы уже в корневой папке").parseMode(ParseMode.HTML)))
        }
        val s = currentPath.substring(0, currentPath.lastIndexOf("/"))
        YandexUtils.setCurrentPath(session, s.substring(0, s.lastIndexOf("/") + 1))
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Предыдущая папка"
    }
}