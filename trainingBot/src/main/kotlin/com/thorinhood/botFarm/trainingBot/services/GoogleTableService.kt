package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.GoogleSheet
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.domain.LessonConfig
import com.thorinhood.botFarm.trainingBot.domain.Task
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import org.springframework.web.client.RestTemplate
import java.util.*

class GoogleTableService(
    private val restTemplate: RestTemplate,
    private val googleTableApiKey: String
) {

    fun uploadLessonToSession(session: Session<Long>) : Lesson? =
        getGoogleSheet(session)?.let { sheet ->
            val size = (session.args[ArgKey.LESSON_CONFIG] as LessonConfig).size
            val lesson = Lesson(
                (0 until size)
                    .map { makeTask(sheet) }
                    .toCollection(LinkedList())
            )
            session.args[ArgKey.LESSON_CURRENT] = lesson
            return lesson
        }

    private fun makeTask(googleSheet: GoogleSheet): Task {
        val rows = googleSheet.values
        val taskIndex = (1 until rows.size).random()
        val questionIndex = (0 until rows[taskIndex].size).random()
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

    private fun <ID> getGoogleSheet(session: Session<ID>): GoogleSheet? {
        return restTemplate.getForEntity(
            "https://sheets.googleapis.com/v4/spreadsheets/${session.args[ArgKey.GOOGLE_TABLE_ID]}/" +
                    "values/${session.args[ArgKey.GOOGLE_TABLE_SHEET]}?alt=json" +
                    "&key=${googleTableApiKey}",
            GoogleSheet::class.java
        ).body
    }
}