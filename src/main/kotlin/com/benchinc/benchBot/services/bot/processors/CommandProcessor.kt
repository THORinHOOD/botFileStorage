package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.AbstractSendRequest

interface CommandProcessor {

    fun getCommandName() : String
    fun process(session: Session, text: String) : AbstractSendRequest<*>

}