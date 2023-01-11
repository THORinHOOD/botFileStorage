package com.thorinhood.botFarm.testBot

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition

@Processor
class WelcomeMessageProcessor : BaseProcessor(
    "welcome",
    ""
) {
    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(null,
            Transition("", "Работает!!!")
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "/start"
    }

}