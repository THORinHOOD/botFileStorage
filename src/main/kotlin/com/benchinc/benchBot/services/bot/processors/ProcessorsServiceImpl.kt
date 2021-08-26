package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.callbacks.CallbackProcessor
import com.benchinc.benchBot.services.bot.processors.commands.CommandProcessor
import com.benchinc.benchBot.services.bot.processors.helpers.extractors.CallbackQueryExtractor
import com.benchinc.benchBot.services.bot.processors.helpers.extractors.CommandTextExtractor
import com.benchinc.benchBot.services.bot.processors.helpers.extractors.LocationExtractor
import com.benchinc.benchBot.services.bot.processors.location.LocationProcessor
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ProcessorsServiceImpl(commandProcessors: List<CommandProcessor>,
                            locationProcessors: List<LocationProcessor>,
                            callbackProcessors: List<CallbackProcessor>,
                            val callbackQueryExtractor: CallbackQueryExtractor,
                            val commandTextExtractor: CommandTextExtractor,
                            val locationExtractor: LocationExtractor) : ProcessorsService {

    val mapCommandProcessors : Map<String, CommandProcessor> = commandProcessors.associateBy { it.commandName }
    val mapCallbackProcessors : Map<String, CallbackProcessor> = callbackProcessors.associateBy { it.callbackName }
    val mapLocationProcessors : Map<String, LocationProcessor> = locationProcessors.associateBy { it.locationName }

    fun <T : AbstractSendRequest<T>> T.addReply(messageId: Int?) : T {
        messageId?.let {
            this.replyToMessageId(it)
        }
        return this
    }

    override fun process(allSessions: AllSessions, update: Update) : List<BaseRequest<*, *>> {
        return locationExtractor.extract(update)?.let { parameter ->
            allSessions.getSession(parameter.chatId).let { session ->
                pipeline(session, parameter.value, null, mapLocationProcessors,
                    "Необходимо прислать локацию") ?:
                processLocation(session, parameter.value)
            }
        }
        ?:
        commandTextExtractor.extract(update)?.let { parameter ->
            allSessions.getSession(parameter.chatId).let { session ->
                pipeline(session, parameter.value.text(), parameter.value.messageId(), mapCommandProcessors,
                    "Необходимо прислать команду") ?:
                processCommand(session, parameter.value)
            }
        }
        ?:
        callbackQueryExtractor.extract(update)?.let { parameter ->
            allSessions.getSession(parameter.chatId).let { session ->
                pipeline(session, parameter.value, null, mapCallbackProcessors,
                    "Необходимо выбрать опцию") ?:
                processCallback(session, parameter.value)
            }
        }
        ?:
        listOf()
    }

    private fun <P : Processor<ARG>, ARG> pipeline(session: Session, arg: ARG, messageId: Int?,
                                                   processors: Map<String, P>,
                                                   wrongParameter: String) : List<BaseRequest<*, *>>? {
        return session.nextProcessor?.let { nextProcessorName ->
            processors[nextProcessorName]?.let { processor ->
                process(processor, session, arg, messageId)
            } ?: listOf(SendMessage(session.chatId, wrongParameter))
        }
    }

    private fun processCommand(session: Session, message: Message) : List<BaseRequest<*, *>> {
        val commandName = extractCommandName(message.text())
        return process(mapCommandProcessors[commandName], session, message.text(), message.messageId())
    }

    private fun processLocation(session: Session, location: Location) : List<BaseRequest<*, *>> {
        return process(mapLocationProcessors["find_benches"], session, location, null)
    }

    private fun processCallback(session: Session, callbackQuery: CallbackQuery) : List<BaseRequest<*, *>> =
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