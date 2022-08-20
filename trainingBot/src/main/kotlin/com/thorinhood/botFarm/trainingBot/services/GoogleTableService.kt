package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.GoogleSheet
import com.thorinhood.botFarm.trainingBot.domain.Task
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
class GoogleTableService(
    private val restTemplate: RestTemplate
) {

    fun <ID> getTask(session: Session<ID>) : Optional<Task> {
        val googleSheet = restTemplate.getForEntity(
            "https://sheets.googleapis.com/v4/spreadsheets/${session.args["google_table_id"]}/" +
                    "values/${session.args["google_table_sheet"]}?alt=json&key=AIzaSyDTxrXX0DKqtDTqIKGjd-KRzWtm_l58AmE",
            GoogleSheet::class.java
        )
        return googleSheet.body?.let { sheet ->
            val rows = sheet.values
            val taskIndex = (1 until rows.size).random()
            val questionIndex = (0 until rows[taskIndex].size).random()
            return Optional.of(
                Task(
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