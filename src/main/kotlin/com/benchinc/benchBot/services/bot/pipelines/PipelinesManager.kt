package com.benchinc.benchBot.services.bot.pipelines

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest

interface PipelinesManager {
    fun process(session: Session, update: Update) : List<BaseRequest<*, *>>
}