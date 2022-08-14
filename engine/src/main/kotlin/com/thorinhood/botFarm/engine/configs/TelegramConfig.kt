package com.thorinhood.botFarm.engine.configs

import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramConfig(
    @Value("\${telegram.token}") private val telegramBotToken : String
) {

    @Bean
    fun telegramBot() : TelegramBot {
        return TelegramBot(telegramBotToken)
    }

}