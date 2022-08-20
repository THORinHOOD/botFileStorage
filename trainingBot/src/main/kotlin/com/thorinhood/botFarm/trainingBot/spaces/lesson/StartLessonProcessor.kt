package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.services.TaskService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class StartLessonProcessor(
    private val taskService: TaskService
) : BaseProcessor(
    "start_lesson",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        session.args[ArgKey.LESSON_TASKS_REMAIN] = (session.args[ArgKey.TIMER_CONFIG] as TimerConfig).size - 1
        return ProcessResult(
            listOf(taskService.buildMessageWithTask(session)),
            Transition(
                ProcSpace.LESSON,
                "Okaaaaay, let's go!",
                ReplyKeyboardMarkup(
                    arrayOf(KeyboardButton("Отмена"))
                )
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Начать занятие")
}