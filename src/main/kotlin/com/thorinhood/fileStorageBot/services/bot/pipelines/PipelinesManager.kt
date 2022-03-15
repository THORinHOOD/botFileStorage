package com.thorinhood.fileStorageBot.services.bot.pipelines

import com.thorinhood.fileStorageBot.data.Session
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest

interface PipelinesManager {
    fun process(session: Session, update: Update) : List<BaseRequest<*, *>>
}