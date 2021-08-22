package com.benchinc.benchBot.services.bot.processors.callbacks

import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.CallbackQuery

interface CallbackProcessor : Processor<CallbackQuery> {
    fun getCallbackName() : String
}