package com.benchinc.benchBot.services.bot.processors.managers

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.callbacks.CallbackProcessor
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.PipelineStrategy
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.ProcessStrategy
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class CallbackProcessorsManager(private val pipelineStrategy: PipelineStrategy,
                                private val processStrategy: ProcessStrategy,
                                callbackProcessors: List<CallbackProcessor>) : ProcessorsManager {

    val mapCallbackProcessors : Map<String, CallbackProcessor> = callbackProcessors.associateBy { it.callbackName }

    override fun process(allSessions: AllSessions, update: Update): List<BaseRequest<*, *>>? =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.message()?.chat()?.id()?.let { chatId ->
                allSessions.getSession(chatId).let { session ->
                    pipelineStrategy.pipeline(session, callbackQuery, null, mapCallbackProcessors,
                        "Необходимо выбрать опцию") ?:
                    processCallback(session, callbackQuery)
                }
            }
        }

    private fun processCallback(session: Session, callbackQuery: CallbackQuery) : List<BaseRequest<*, *>> =
        processStrategy.process(mapCallbackProcessors[callbackQuery.data()], session, callbackQuery, null)

}