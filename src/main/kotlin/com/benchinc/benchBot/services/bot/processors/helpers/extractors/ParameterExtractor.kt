package com.benchinc.benchBot.services.bot.processors.helpers.extractors

import com.benchinc.benchBot.services.bot.processors.helpers.data.Parameter
import com.pengrad.telegrambot.model.Update

interface ParameterExtractor<PARAMETER> {
    fun extract(update: Update) : Parameter<PARAMETER>?
}