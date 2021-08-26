package com.benchinc.benchBot.services.bot.processors.helpers.extractors

import com.benchinc.benchBot.services.bot.processors.helpers.data.Parameter
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Update
import org.springframework.stereotype.Service

@Service
class CallbackQueryExtractor : ParameterExtractor<CallbackQuery> {

    override fun extract(update: Update): Parameter<CallbackQuery>? {
        return update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.message()?.chat()?.id()?.let { chatId ->
                Parameter(callbackQuery, chatId)
            }
        }
    }

}