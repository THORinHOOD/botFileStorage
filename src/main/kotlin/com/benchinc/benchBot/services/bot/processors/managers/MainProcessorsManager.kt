package com.benchinc.benchBot.services.bot.processors.managers

import com.benchinc.benchBot.data.AllSessions
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest

interface MainProcessorsManager {
    fun process(allSessions: AllSessions, update: Update) : List<BaseRequest<*, *>>
}