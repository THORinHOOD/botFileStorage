package com.thorinhood.botFarm.telegram

import com.pengrad.telegrambot.TelegramBot
import com.thorinhood.botFarm.engine.messages.Converter
import com.thorinhood.botFarm.engine.messages.BotSender
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.scheduling.annotation.Async

open class TelegramBotSender<IS>(
    private val telegramBot: TelegramBot,
    override val converter: Converter<IS, TelegramSendMessage>
) : BotSender<TelegramSendMessage, IS>, Logging {

    @Async
    override fun sendMessages(messages: List<IS>) {
        messages.forEach { sendMessage(it) }
    }

    @Async
    override fun sendMessage(message: IS) {
        converter.convert(message).let {
            try {
                telegramBot.execute(it)
            } catch(exception: Exception) {
                logger.error(exception)
            }
        }
    }
}