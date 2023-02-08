package com.thorinhood.botFarm.trainingBot.spaces.subject

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class BackSubjectSelectProcessor(
    private val argumentsDataService: SessionArgumentsDataService
) : BaseCancelProcessor(
    "back_subject_select",
    ProcSpace.IN_SUBJECT,
    Transition(
        ProcSpace.DEFAULT,
        "Окей, что дальше?",
        KeyboardMarkups.DEFAULT_KEYBOARD
    ) { session ->
        argumentsDataService.maintainWrap(session.sessionId) {
            it.remove(ArgKey.SELECTED_SUBJECT)
        }
    }
) {
    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, "Назад")
}