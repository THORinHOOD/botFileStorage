package com.thorinhood.botFarm.engine.messages

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.scheduling.annotation.Async

class TelegramMsgService(
    private val telegramBot: TelegramBot
): MsgSender<BaseRequest<*, *>> {

    @Async
    override fun sendMessages(messages: List<BaseRequest<*, *>>) {
        messages.forEach { telegramBot.execute(it) }
    }

    @Async
    override fun sendMessage(message: BaseRequest<*, *>) {
        telegramBot.execute(message)
    }

}