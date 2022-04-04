package com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

abstract class BaseCancelProcessor(
    name: String,
    procSpace: String = "",
    private val transition: Transition
) : BaseProcessor(name, procSpace) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(null, transition, null)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Отмена"
    }

}