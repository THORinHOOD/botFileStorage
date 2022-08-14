package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.botFarm.engine.processors.data.ProcessResult

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.sessions.Session

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
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        val currentPath = YandexUtils.getCurrentPath(session)
        if (currentPath == "disk:/") {
            return ProcessResult(listOf(SendMessage(session.sessionId, "Вы уже в корневой папке").parseMode(ParseMode.HTML)))
        }
        val s = currentPath.substring(0, currentPath.lastIndexOf("/"))
        YandexUtils.setCurrentPath(session, s.substring(0, s.lastIndexOf("/") + 1))
        YandexUtils.getContext(session).offset = 0
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Предыдущая папка"
    }
}