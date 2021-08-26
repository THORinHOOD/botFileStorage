package com.benchinc.benchBot.services.bot.processors.helpers.extractors

import com.benchinc.benchBot.services.bot.processors.helpers.data.Parameter
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.model.Update
import org.springframework.stereotype.Service

@Service
class LocationExtractor : ParameterExtractor<Location> {

    override fun extract(update: Update): Parameter<Location>? {
        return update.message()?.location()?.let { location ->
            update.message().chat().id().let { chatId ->
                Parameter(location, chatId)
            }
        }
    }

}