package com.benchinc.benchBot.services.bot.processors.managers

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.commands.CommandProcessor
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.PipelineStrategy
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.ProcessStrategy
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class CommandProcessorsManager(private val pipelineStrategy: PipelineStrategy,
                               private val processStrategy: ProcessStrategy,
                               commandProcessors: List<CommandProcessor>
) : ProcessorsManager {

    private val mapCommandProcessors : Map<String, CommandProcessor> = commandProcessors.associateBy { it.commandName }

    override fun process(allSessions: AllSessions, update: Update): List<BaseRequest<*, *>>? =
        update.message()?.let { message ->
            update.message().chat().id().let { chatId ->
                allSessions.getSession(chatId).let { session ->
                    pipelineStrategy.pipeline(session, message.text(), message.messageId(),
                        mapCommandProcessors, "Необходимо прислать комманду") ?:
                    processCommand(session, message)
                }
            }
        }

    private fun processCommand(session: Session, message: Message): List<BaseRequest<*, *>> {
        val commandName = extractCommandName(message.text())
        return processStrategy.process(mapCommandProcessors[commandName], session, message.text(), message.messageId())
    }

    private fun extractCommandName(text: String) : String {
        if (!text.startsWith("/")) {
            return text
        }
        val indexRight = text.indexOf("_").takeIf { it != -1 } ?: text.length
        return text.substring(1, indexRight)
    }

}