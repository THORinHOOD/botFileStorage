package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest

interface Processor {
    val name: String

    fun process(session: Session, update: Update) : List<BaseRequest<*, *>>
    fun isThisProcessorMessage(update: Update) : Boolean
}