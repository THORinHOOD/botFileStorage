package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.entity_manipulation.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces

@Processor
class StartDeleteEntityProcessor : BaseProcessor(
    "startDelete",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult = ProcessResult(
        listOf(SendMessage(session.chatId, "Удалить перманентно или переместить в корзину?")
            .replyMarkup(KeyboardService.FILE_TREE_DELETE_CHOICE)),
        Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_DELETE)
    )

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Удалить"
    }
}