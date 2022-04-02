package com.thorinhood.fileStorageBot.chatBotEngine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import java.io.PrintWriter
import java.io.StringWriter

@Service
class ProcessorsManager(
    @Processor private val processors: List<BaseProcessor>
) : Logging {

    fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val foundProcessors = processors.filter { it.isThisProcessor(session, update) }.toList()
        return if (foundProcessors.size > 1) {
            logger.error("Found more than 1 processor [${session.cursor.procSpace}]")
            ERROR_MESSAGE(session.chatId)
        } else if (foundProcessors.isEmpty()) {
            logger.error("Not found any processors [${session.cursor.procSpace}]")
            ERROR_MESSAGE(session.chatId)
        } else {
            try {
                foundProcessors.first().process(session, update)
            } catch(e: Exception) {
                error(e)
                ERROR_MESSAGE(session.chatId)
            }
        }
    }

    private fun error(e: Exception) {
        val stacktrace = StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
        logger.error(stacktrace)
    }

    companion object {
        private fun ERROR_MESSAGE(chatId: Long) = listOf(SendMessage(chatId, "Что-то пошло не так"))
    }
}