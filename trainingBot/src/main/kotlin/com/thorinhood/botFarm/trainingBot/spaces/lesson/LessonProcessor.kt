package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.services.TaskService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class LessonProcessor(
    private val taskService: TaskService
) : BaseProcessor(
    "lesson", ProcSpace.LESSON
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val answers = session.cursor.args["answers"] as List<*>
        if (answers.contains(update.message()?.text()?.lowercase())) {
            session.args[ArgKey.LESSON_TASKS_REMAIN] = (session.args[ArgKey.LESSON_TASKS_REMAIN] as Int) - 1
            if (session.args[ArgKey.LESSON_TASKS_REMAIN] as Int >= 0) {
                taskService.buildMessageWithTask(session)
                return ProcessResult(
                    listOf(
                        SendMessage(
                            session.sessionId, "Правильно!"
                        ), taskService.buildMessageWithTask(session)
                    )
                )
            } else {
                session.cursor.args.remove("answers")
                return ProcessResult(
                    null, Transition(
                        ProcSpace.DEFAULT, "Правильно!\n" + "Ты молодец!\n" + "До следующего задания!",
                        KeyboardMarkups.DEFAULT_KEYBOARD
                    )
                )
            }
        } else {
            return ProcessResult(
                listOf(
                    SendMessage(
                        session.sessionId, "Неправильно, попробуй снова"
                    )
                )
            )
        }
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean = isNotCancel(update)
}