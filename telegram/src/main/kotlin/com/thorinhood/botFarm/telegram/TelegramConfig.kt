package com.thorinhood.botFarm.telegram

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.messages.Converter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@ComponentScan("com.thorinhood.*")
@Configuration
class TelegramConfig(
    @Value("\${telegram.token}") private val telegramBotToken: String
) {

    @Bean
    fun telegramBot(): TelegramBot = TelegramBot(telegramBotToken)

}