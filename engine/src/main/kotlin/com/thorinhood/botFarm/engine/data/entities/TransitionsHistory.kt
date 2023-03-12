package com.thorinhood.botFarm.engine.data.entities

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.LinkedList

@Document("transition_history")
open class TransitionsHistory(
    @Id override val sessionId: Long,
    val history: LinkedList<String> = LinkedList<String>()
) : Entity, Logging {

    protected fun getCurrentProcSpace(defaultProcSpace: String): String =
        if (history.isEmpty()) {
            defaultProcSpace
        } else {
            history.first
        }

    protected fun makeTransition(procSpace: String, capacity: Int) {
        history.addFirst(procSpace)
        while (history.size > capacity) {
            history.removeLast()
        }
    }

    fun makeBackTransition() {
        if (history.isEmpty()) {
            logger.error("Thrown BackTransition when transitions history is empty!")
            throw IndexOutOfBoundsException("Thrown BackTransition when transitions history is empty!")
        }
        history.removeFirst()
    }

}