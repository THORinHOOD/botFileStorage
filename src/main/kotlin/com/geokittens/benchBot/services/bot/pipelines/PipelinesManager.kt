package com.geokittens.benchBot.services.bot.pipelines

import com.geokittens.benchBot.data.Session
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest

interface PipelinesManager {
    fun process(session: Session, update: Update) : List<BaseRequest<*, *>>
}