package com.benchinc.benchBot.services.bot.processors.helpers.strategies

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ProcessStrategy {

    fun <T : AbstractSendRequest<T>> T.addReply(messageId: Int?) : T {
        messageId?.let {
            this.replyToMessageId(it)
        }
        return this
    }

    fun <P : Processor<ARG>, ARG> process(processor: P?, session : Session, arg: ARG, messageId: Int?)
            : List<BaseRequest<*, *>> {
        return processor?.process(session, arg) ?:
        listOf(SendMessage(session.chatId, "Неизвестная команда").addReply(messageId))
    }

}