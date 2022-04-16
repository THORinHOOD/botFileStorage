package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

@Processor
class StartCreateFolderProcessor : BaseProcessor(
    "startCreateFolder",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        return ProcessResult(
            listOf(SendMessage(session.sessionId, "Напишите имя папки\nПапка будет создана внутри текущей папки " +
                "[${YandexUtils.getCurrentPath(session)}]")
            .replyMarkup(KeyboardService.FILE_TREE_CREATE_FOLDER_KEYBOARD)),
            Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_CREATE_FOLDER))
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Создать папку"
    }
}