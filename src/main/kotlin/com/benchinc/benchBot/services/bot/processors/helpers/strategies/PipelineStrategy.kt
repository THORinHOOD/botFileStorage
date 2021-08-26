package com.benchinc.benchBot.services.bot.processors.helpers.strategies

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class PipelineStrategy(private val processStrategy: ProcessStrategy) {

    fun <P : Processor<ARG>, ARG> pipeline(session: Session, arg: ARG, messageId: Int?,
                           processors: Map<String, P>,
                           wrongParameter: String) : List<BaseRequest<*, *>>? {
        return session.nextProcessor?.let { nextProcessorName ->
            processors[nextProcessorName]?.let { processor ->
                processStrategy.process(processor, session, arg, messageId)
            } ?: listOf(SendMessage(session.chatId, wrongParameter))
        }
    }

}