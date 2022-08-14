package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class StartDeleteEntityProcessor : BaseProcessor(
    "startDelete",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult = ProcessResult(
        listOf(SendMessage(session.sessionId, "Удалить перманентно или переместить в корзину?")
            .replyMarkup(KeyboardService.FILE_TREE_DELETE_CHOICE)),
        Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_DELETE)
    )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Удалить"
    }
}