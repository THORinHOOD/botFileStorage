package com.thorinhood.botFarm.trainingBot.spaces

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class WelcomeProcessor : BaseProcessor(
    "welcome",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session, update: Update): ProcessResult =
        ProcessResult(
            null,
            Transition(
                ProcSpace.DEFAULT,
                "Привет! ${Emojis.HAND_WAVE}",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        private const val LABEL = "/start"
    }
}