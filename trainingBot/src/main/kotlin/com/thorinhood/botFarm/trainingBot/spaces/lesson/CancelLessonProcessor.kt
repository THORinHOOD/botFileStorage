package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelLessonProcessor(
    private val lessonService: LessonService
) : BaseCancelProcessor(
    "cancel_lesson",
    ProcSpace.LESSON,
    Transition(
        ProcSpace.DEFAULT,
        "Okaaaay, let's stop!",
        KeyboardMarkups.DEFAULT_KEYBOARD
    ) { session -> lessonService.stopLesson(session) }
) {
    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, "Закончить занятие")
}