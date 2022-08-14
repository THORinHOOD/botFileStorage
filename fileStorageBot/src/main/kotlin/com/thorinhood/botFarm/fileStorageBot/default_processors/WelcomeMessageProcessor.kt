package com.thorinhood.botFarm.fileStorageBot.default_processors

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class WelcomeMessageProcessor(
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "welcome",
    ProcSpaces.WILD_CARD
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        return ProcessResult(null,
            Transition(ProcSpaces.DEFAULT, "Привет", keyboardService.getDefaultKeyboard(session))
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "/start"
    }
}