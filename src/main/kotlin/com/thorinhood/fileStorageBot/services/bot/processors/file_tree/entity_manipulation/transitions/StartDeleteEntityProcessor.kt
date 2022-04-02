package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.entity_manipulation.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.services.bot.KeyboardService

@Processor
class StartDeleteEntityProcessor : BaseProcessor(
    "startDelete",
    "file_tree#entity_manipulation"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult = ProcessResult(
        listOf(SendMessage(session.chatId, "Удалить перманентно или переместить в корзину?")
            .replyMarkup(KeyboardService.FILE_TREE_DELETE_CHOICE)),
        Transition("file_tree#entity_manipulation#delete")
    )

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Удалить"
    }
}