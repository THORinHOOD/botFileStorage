package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.*
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class LessonService(
    private val restTemplate: RestTemplate,
    @Value("\${google.table.api.key}") private val googleTableApiKey: String
) {

    fun startLesson(session: Session<Long>) : List<BaseRequest<*, *>> {
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
        val subject = subjects[session.args[ArgKey.SELECTED_SUBJECT]]!!
        return startLesson(session, subject)
    }

    fun startLesson(session: Session<Long>, subject: Subject) : List<BaseRequest<*, *>> {
        session.procSpace = ProcSpace.LESSON
        val lesson = makeLesson(subject) ?: throw Exception("Не получилось собрать задание")
        session.args[ArgKey.LESSON] = lesson
        return listOf(
            SendMessage(
                session.sessionId,
                "Пора начинать занятие \"${subject.name}\"!"
            ).replyMarkup(KeyboardMarkups.LESSON_KEYBOARD),
            makeCurrentTaskMessage(session.sessionId, lesson)
        )
    }

    fun makeCurrentTaskMessage(sessionId: Long, lesson: Lesson) : BaseRequest<*, *> {
        return SendMessage(sessionId, "Переведи:\n${lesson.getCurrentTask().question}")
    }

    fun makeLesson(subject: Subject) : Lesson? =
        getGoogleSheet(subject)?.let { sheet ->
            return Lesson(
                (0 until subject.lessonSize)
                    .map { makeTask(sheet) }
                    .toCollection(LinkedList())
            )
        }

    private fun makeTask(googleSheet: GoogleSheet): Task {
        val rows = googleSheet.values
        val taskIndex = (1 until rows.size).random()
        val questionIndex = rows[taskIndex].indices.filter { i ->
            rows[taskIndex][i].isNotEmpty() &&
                    rows[taskIndex][i].isNotBlank()
        }.random()
        return Task(
            rows[taskIndex][questionIndex],
            rows[taskIndex].filterIndexed { i, _ ->
                (i != questionIndex) &&
                        (rows[0][questionIndex] != rows[0][i]) &&
                        rows[taskIndex][i].isNotEmpty() &&
                        rows[taskIndex][i].isNotBlank()
            }.map {
                it.lowercase()
            }
        )
    }

    private fun getGoogleSheet(subject: Subject): GoogleSheet? {
        return restTemplate.getForEntity(
            "https://sheets.googleapis.com/v4/spreadsheets/${subject.googleTableId}/" +
                    "values/${subject.googleTableSheet}?alt=json&key=${googleTableApiKey}",
            GoogleSheet::class.java
        ).body
    }
}