package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.callbacks.CallbackProcessor
import com.benchinc.benchBot.services.bot.processors.commands.CommandProcessor
import com.benchinc.benchBot.services.bot.processors.location.LocationProcessor
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ProcessorsService(commandProcessors: List<CommandProcessor>,
                        val findBenchesProcessor: LocationProcessor,
                        callbackProcessors: List<CallbackProcessor>) {

    val mapCommandProcessors : Map<String, CommandProcessor> = commandProcessors.associateBy { it.commandName }
    val mapCallbackProcessors : Map<String, CallbackProcessor> = callbackProcessors.associateBy { it.callbackName }

    fun <T : AbstractSendRequest<T>> T.addReply(messageId: Int?) : T {
        messageId?.let {
            this.replyToMessageId(it)
        }
        return this
    }

    fun processCommand(session: Session, messageId: Int, text: String) : List<BaseRequest<*, *>> {
        val commandName = extractCommandName(text)
        return process(mapCommandProcessors[commandName], session, text, messageId)
    }

    fun processLocation(session: Session, location: Location) : List<BaseRequest<*, *>> {
        return findBenchesProcessor.process(session, location)
    }

    fun processCallback(session: Session, callbackQuery: CallbackQuery) : List<BaseRequest<*, *>> =
        process(mapCallbackProcessors[callbackQuery.data()], session, callbackQuery, null)

    private fun <P : Processor<ARG>, ARG> process(processor: P?, session : Session, arg: ARG, messageId: Int?)
        : List<BaseRequest<*, *>> {
        return processor?.process(session, arg) ?:
            listOf(SendMessage(session.chatId, "Неизвестная команда").addReply(messageId))
    }

    private fun extractCommandName(text: String) : String {
        if (!text.startsWith("/")) {
            return text
        }
        val indexRight = text.indexOf("_").takeIf { it != -1 } ?: text.length
        return text.substring(1, indexRight)
    }

}