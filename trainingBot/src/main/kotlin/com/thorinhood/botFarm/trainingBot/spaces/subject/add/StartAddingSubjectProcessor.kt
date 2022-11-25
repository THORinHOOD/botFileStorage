package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class StartAddingSubjectProcessor : BaseProcessor(
    "start_adding_subject",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        return ProcessResult(
            null,
            Transition(
                ProcSpace.INPUT_SUBJECT_NAME,
                "Окей, введи имя нового предмета",
                KeyboardMarkups.CANCEL_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Добавить предмет")

}