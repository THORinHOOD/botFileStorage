package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class WelcomeMessageProcessor(
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "welcome",
    ""
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(null,
            Transition("default", "Привет", keyboardService.getDefaultKeyboard(session))
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "/start"
    }
}