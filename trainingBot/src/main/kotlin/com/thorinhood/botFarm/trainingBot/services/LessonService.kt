package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.stereotype.Service

@Service
class LessonService(
    private val googleTableService: GoogleTableService
) {

    fun stopLesson(session: Session<Long>) {
        session.args.remove(ArgKey.TASK_CURRENT)
        session.args.remove(ArgKey.LESSON_CURRENT)
    }

    fun startLesson(session: Session<Long>) : List<BaseRequest<*, *>> {
        session.cursor.procSpace = ProcSpace.LESSON
        googleTableService.uploadLessonToSession(session)
        return listOf(
            SendMessage(
                session.sessionId,
                "Пора начинать занятие!"
            ).replyMarkup(KeyboardMarkups.LESSON_KEYBOARD),
            takeNextTask(session)
        )
    }

    fun takeNextTask(session: Session<Long>) : BaseRequest<*, *> {
        val task = (session.args[ArgKey.LESSON_CURRENT] as Lesson).tasks.poll()
        session.args[ArgKey.TASK_CURRENT] = task
        return SendMessage(
            session.sessionId,
            "Переведи:\n${task.question}"
        )
    }

    fun hasNextTask(session: Session<Long>) : Boolean {
        val lesson = session.args[ArgKey.LESSON_CURRENT] as Lesson
        return lesson.tasks.isNotEmpty()
    }

}