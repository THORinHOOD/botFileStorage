package com.geokittens.benchBot.services

import com.geokittens.benchBot.data.AllSessions
import com.geokittens.benchBot.data.Session
import com.pengrad.telegrambot.model.Update
import org.springframework.stereotype.Service

@Service
class AllSessionsService {

    private val sessions: AllSessions = AllSessions()

    fun getSession(update: Update) : Session {
        return sessions.getSession(
            update.message()?.chat()?.id() ?:
            update.callbackQuery()?.message()?.chat()?.id() ?:
            update.editedMessage()?.chat()?.id() ?:
            throw IllegalArgumentException("Can't find chat id in update"))
    }

}