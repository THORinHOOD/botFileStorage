package com.thorinhood.botFarm.engine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import com.thorinhood.botFarm.engine.extractSessionId
import com.thorinhood.botFarm.engine.processors.data.Session
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.io.PrintWriter
import java.io.StringWriter

@Service
class ProcessorsManager(
    @Processor processors: List<BaseProcessor>,
    private val transactionsHistoryDataService: TransactionsHistoryDataService,
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
        val sessionId = update.extractSessionId()
        val transitionsHistory = transactionsHistoryDataService.getBySessionId(sessionId)
        val sessionProcSpace = transitionsHistory.getCurrentProcSpace()
        val session = Session(sessionId, transitionsHistory)
        val processors = mutableListOf<BaseProcessor>()
        if (!spaces.containsKey(sessionProcSpace)) {
            logger.error("Can't find procSpace = $sessionProcSpace")
            return ERROR_MESSAGE(sessionId)
        }
        if (spaces.containsKey("")) {
            processors.addAll(spaces[""]!!)
        }
        processors.addAll(spaces[sessionProcSpace]!!)
        val foundProcessors = processors.filter { it.isThisProcessor(session, update) }.toSet()
        return if (foundProcessors.size > 1) {
            logger.error("Found more than 1 processor \nprocSpace = $sessionProcSpace" +
                    "\nprocessors = ${foundProcessors.map { it.name }}")
            ERROR_MESSAGE(sessionId)
        } else if (foundProcessors.isEmpty()) {
            logger.error("Not found any processors \nprocSpace = $sessionProcSpace")
            listOf(SendMessage(sessionId, "Что-то я тебя не понимаю"))
        } else {
            try {
                val messages = foundProcessors.first().process(session, update)
                transactionsHistoryDataService.update(transitionsHistory)
                messages
            } catch(e: Exception) {
                error(e)
                ERROR_MESSAGE(sessionId)
            }
        }
    }

    private fun error(e: Exception) {
        val stacktrace = StringWriter().also { e.printStackTrace(PrintWriter(it)) }.toString().trim()
        logger.error(stacktrace)
    }

    companion object {
        private fun ERROR_MESSAGE(sessionId: Long) = listOf(SendMessage(sessionId, "Что-то пошло не так"))
    }
}