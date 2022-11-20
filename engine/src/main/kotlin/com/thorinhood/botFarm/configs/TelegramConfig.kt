package com.thorinhood.botFarm.configs

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.messages.MsgSender
import com.thorinhood.botFarm.engine.messages.TelegramMsgService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

typealias TelegramMessage = BaseRequest<*, *>

@Configuration
class TelegramConfig(
    @Value("\${telegram.token}") private val telegramBotToken : String
) {

    @Bean
    fun telegramBot() : TelegramBot {
        return TelegramBot(telegramBotToken)
    }

    @Bean
    fun telegramMsgService(telegramBot: TelegramBot) : MsgSender<TelegramMessage> =
        TelegramMsgService(telegramBot)

}