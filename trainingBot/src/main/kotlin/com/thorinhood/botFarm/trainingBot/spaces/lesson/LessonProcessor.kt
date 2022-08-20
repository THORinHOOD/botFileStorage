package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.Task
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class LessonProcessor(
    private val lessonService: LessonService
) : BaseProcessor(
    "lesson", ProcSpace.LESSON
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val task = session.args[ArgKey.TASK_CURRENT] as Task
        if (task.answers.contains(update.message()?.text()?.lowercase())) {
            if (lessonService.hasNextTask(session)) {
                val nextTaskMessage = lessonService.takeNextTask(session)
                return ProcessResult(
                    listOf(
                        SendMessage(session.sessionId, "Правильно!"),
                        nextTaskMessage
                    )
                )
            } else {
                lessonService.stopLesson(session)
                return ProcessResult(
                    null,
                    Transition(
                        ProcSpace.DEFAULT,
                        "Правильно!\n" + "Ты молодец!\n" + "До следующего задания!",
                        KeyboardMarkups.DEFAULT_KEYBOARD
                    )
                )
            }
        } else {
            return ProcessResult(listOf(SendMessage(session.sessionId, "Неправильно, попробуй снова")))
        }
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        !isUpdateMessageEqualsLabel(update, "Закончить занятие") &&
        !isUpdateMessageEqualsLabel(update, "Не знаю")
}