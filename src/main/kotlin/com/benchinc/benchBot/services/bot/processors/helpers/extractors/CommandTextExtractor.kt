package com.benchinc.benchBot.services.bot.processors.helpers.extractors

import com.benchinc.benchBot.services.bot.processors.helpers.data.Parameter
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.springframework.stereotype.Service

@Service
class CommandTextExtractor : ParameterExtractor<Message> {

    override fun extract(update: Update): Parameter<Message>? {
        return update.message()?.let { message ->
            update.message().chat().id().let { chatId ->
                Parameter(message, chatId)
            }
        }
    }

}