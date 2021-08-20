package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.CommandProcessor
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class CommandsProcessorsService(processors: List<CommandProcessor>) {

    val mapProcessors : Map<String, CommandProcessor> = processors.associateBy { it.getCommandName() }

    fun process(session: Session, messageId: Int, text: String) : AbstractSendRequest<*> {
        val commandName = extractCommandName(text)
        val processor = mapProcessors[commandName]
        return processor?.process(session, text) ?:
                SendMessage(session.chatId, "Неизвестная команда").replyToMessageId(messageId)
    }

    private fun extractCommandName(text: String) : String {
        if (!text.startsWith("/")) {
            return text
        }
        var indexRight = text.indexOf("_")
        if (indexRight == -1) {
            indexRight = text.length
        }
        return text.substring(1, indexRight)
    }

}