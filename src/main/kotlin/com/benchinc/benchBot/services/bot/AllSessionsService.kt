package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.data.Session
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

    fun getSession(chatId: Long) : Session = sessions.getSession(chatId)

}