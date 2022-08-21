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
        session.args.remove(ArgKey.LESSON_CURRENT)
    }

    fun startLesson(session: Session<Long>) : List<BaseRequest<*, *>> {
        session.cursor.procSpace = ProcSpace.LESSON
        val lesson = googleTableService.uploadLessonToSession(session)
            ?: throw Exception("Не получилось собрать задание")
        return listOf(
            SendMessage(
                session.sessionId,
                "Пора начинать занятие!"
            ).replyMarkup(KeyboardMarkups.LESSON_KEYBOARD),
            makeCurrentTaskMessage(session.sessionId, lesson)
        )
    }

    fun makeCurrentTaskMessage(sessionId: Long, lesson: Lesson) : BaseRequest<*, *> {
        return SendMessage(sessionId, "Переведи:\n${lesson.getCurrentTask().question}")
    }

}