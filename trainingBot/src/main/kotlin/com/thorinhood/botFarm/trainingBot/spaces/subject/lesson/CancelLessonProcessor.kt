package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelLessonProcessor: BaseCancelProcessor(
    "cancel_lesson",
    ProcSpace.LESSON,
    Transition(
        ProcSpace.DEFAULT,
        "Okaaaay, let's stop!",
        KeyboardMarkups.DEFAULT_KEYBOARD
    ) { session ->
        session.args.remove(ArgKey.LESSON)
        session.args.remove(ArgKey.SELECTED_SUBJECT)
    }
) {
    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, "Закончить занятие")
}