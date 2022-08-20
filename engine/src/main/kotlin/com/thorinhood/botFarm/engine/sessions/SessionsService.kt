package com.thorinhood.botFarm.engine.sessions

import com.pengrad.telegrambot.model.Update

interface SessionsService {

    fun getSession(update: Update) : Session<Long>

    fun updateSession(session: Session<Long>)

    fun extractSessionId(update: Update) : Long =
        update.message()?.chat()?.id() ?:
        update.callbackQuery()?.message()?.chat()?.id() ?:
        update.editedMessage()?.chat()?.id() ?:
        throw IllegalArgumentException("Can't find chat id in update")

    fun getAllSessions() : List<Session<Long>>
}