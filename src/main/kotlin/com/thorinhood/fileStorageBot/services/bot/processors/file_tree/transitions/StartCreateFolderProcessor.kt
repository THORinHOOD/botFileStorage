package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.services.bot.KeyboardService

@Processor
class StartCreateFolderProcessor : BaseProcessor(
    "startCreateFolder",
    "file_tree"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(
            listOf(SendMessage(session.chatId, "Напишите имя папки\nПапка будет создана внутри текущей папки " +
                "[${session.fileTreeInfo.currentPath}]")
            .replyMarkup(KeyboardService.FILE_TREE_CREATE_FOLDER_KEYBOARD)),
            Transition("file_tree#entity_manipulation#create_folder"))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Создать папку"
    }
}