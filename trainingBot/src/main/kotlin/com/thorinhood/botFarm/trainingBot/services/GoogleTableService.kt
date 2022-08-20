package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.GoogleSheet
import com.thorinhood.botFarm.trainingBot.domain.Question
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class GoogleTableService(
    private val restTemplate: RestTemplate
) {

    fun <ID> getTask(session: Session<ID>) : Optional<Question> {
        val googleSheet = restTemplate.getForEntity(
            "https://sheets.googleapis.com/v4/spreadsheets/${session.args[ArgKey.GOOGLE_TABLE_ID]}/" +
                    "values/${session.args[ArgKey.GOOGLE_TABLE_SHEET]}?alt=json" +
                    "&key=AIzaSyDTxrXX0DKqtDTqIKGjd-KRzWtm_l58AmE",
            GoogleSheet::class.java
        )
        return googleSheet.body?.let { sheet ->
            val rows = sheet.values
            val taskIndex = (1 until rows.size).random()
            val questionIndex = (0 until rows[taskIndex].size).random()
            return Optional.of(
                Question(
                    rows[taskIndex][questionIndex],
                    rows[taskIndex].filterIndexed { i, _ ->
                        (i != questionIndex) &&
                        (rows[0][questionIndex] != rows[0][i])
                    }.map {
                        it.lowercase()
                    }
                )
            )
        } ?: return Optional.empty()
    }

}