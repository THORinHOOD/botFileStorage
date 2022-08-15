package com.thorinhood.botFarm.engine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.SessionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.io.PrintWriter
import java.io.StringWriter

@Service
class ProcessorsManager(
    @Processor processors: List<BaseProcessor>,
    private val sessionsService: SessionsService,
    private val appContext: ApplicationContext
) : Logging {

    private val spaces: MutableMap<String, MutableList<BaseProcessor>> = mutableMapOf()

    init {
//        appContext.beanDefinitionNames.toList().stream().map { it.toLowerCase() }.filter { it.contains("mon") }.forEach {
//            println(it)
//        }
        processors.forEach { processor ->
            spaces.getOrPut(processor.procSpace) { mutableListOf() }
                .add(processor)
        }
    }

    fun process(update: Update): List<BaseRequest<*, *>> {
        val session = sessionsService.getSession(update)
        val processors = mutableListOf<BaseProcessor>()
        if (!spaces.containsKey(session.cursor.procSpace)) {
            logger.error("Can't find proc space ${session.cursor.procSpace}")
            return com.thorinhood.botFarm.engine.processors.ProcessorsManager.Companion.ERROR_MESSAGE(session.sessionId)
        }
        if (spaces.containsKey("")) {
            processors.addAll(spaces[""]!!)
        }
        processors.addAll(spaces[session.cursor.procSpace]!!)
        val foundProcessors = processors.filter { it.isThisProcessor(session, update) }.toList()
        return if (foundProcessors.size > 1) {
            logger.error("Found more than 1 processor [${session.cursor.procSpace}]")
            ERROR_MESSAGE(session.sessionId)
        } else if (foundProcessors.isEmpty()) {
            logger.error("Not found any processors [${session.cursor.procSpace}]")
            ERROR_MESSAGE(session.sessionId)
        } else {
            try {
                val result = foundProcessors.first().process(session, update)
                sessionsService.updateSession(session)
                result
            } catch(e: Exception) {
                error(e)
                ERROR_MESSAGE(session.sessionId)
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