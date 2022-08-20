package com.thorinhood.botFarm.engine.sessions.memory

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.configs.MainConfig
import com.thorinhood.botFarm.engine.Utils
import com.thorinhood.botFarm.engine.sessions.Cursor
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

class MemorySessionsService(
    private val cursorProcSpaceInit: String
) : SessionsService, Logging {

    private val allSessions: MutableMap<Long, MemorySession<Long>> = mutableMapOf()

    init {
        Utils.initLog(logger, "memory")
    }

    override fun getSession(update: Update): Session<Long> {
        val chatId = extractSessionId(update)
        return allSessions.computeIfAbsent(chatId) {
            MemorySession(
                chatId,
                Cursor(cursorProcSpaceInit),
                mutableMapOf()
            )
        }
    }

    override fun updateSession(session: Session<Long>) {
        if (session is MemorySession<Long>) {
            allSessions[session.sessionId] = session
        } else {
            throw IllegalArgumentException("You use in memory storage but session is not MemorySession")
        }
    }

    override fun getAllSessions(): List<Session<Long>> = allSessions.values.toList()

}