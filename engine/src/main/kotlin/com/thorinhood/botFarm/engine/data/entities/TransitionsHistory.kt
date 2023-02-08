package com.thorinhood.botFarm.engine.data.entities

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.data.BackTransition
import com.thorinhood.botFarm.engine.processors.data.BaseTransition
import com.thorinhood.botFarm.engine.processors.data.Transition
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.lang.IllegalArgumentException
import java.util.LinkedList

@Document("transition_history")
class TransitionsHistory(
    @Id override val sessionId: Long,
    val defaultProcSpace: String,
    val capacity: Int = 5,
    val history: LinkedList<String> = LinkedList<String>()
) : Entity, Logging {
    fun makeTransition(sessionId: Long, transition: BaseTransition?): SendMessage? =
        transition?.let {
            when(it) {
                is Transition -> processTransition(it.procSpace)
                is BackTransition -> processBackTransition()
                else -> throw IllegalArgumentException("Unknown transition type: ${it.javaClass}")
            }
            it.makeMessage(sessionId)
        }

    fun getCurrentProcSpace(): String =
        if (history.isEmpty()) {
            defaultProcSpace
        } else {
            history.first
        }

    fun processTransition(procSpace: String) {
        history.addFirst(procSpace)
        while (history.size > capacity) {
            history.removeLast()
        }
    }

    private fun processBackTransition() {
        if (history.isEmpty()) {
            logger.error("Thrown BackTransition when transitions history is empty!")
            throw IndexOutOfBoundsException("Thrown BackTransition when transitions history is empty!")
        }
        history.removeFirst()
    }

}