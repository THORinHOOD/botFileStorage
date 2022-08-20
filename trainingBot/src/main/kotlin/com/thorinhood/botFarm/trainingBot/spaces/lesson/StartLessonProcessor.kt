package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class StartLessonProcessor(
    private val lessonService: LessonService
) : BaseProcessor(
    "start_lesson",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult =
        ProcessResult(
            lessonService.startLesson(session),
            Transition(
                ProcSpace.LESSON,
                "Okaaaaay, let's go!",
                KeyboardMarkups.LESSON_KEYBOARD
            )
        )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Начать занятие")
}