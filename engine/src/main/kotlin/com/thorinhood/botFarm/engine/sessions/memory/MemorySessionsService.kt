package com.thorinhood.botFarm.engine.sessions.memory

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.engine.sessions.TransitionsHistory
import org.apache.logging.log4j.kotlin.Logging

class MemorySessionsService(
    private val procSpaceInit: String,
    private val historyCapacity: Int
) : SessionsService, Logging {

    private val allSessions: MutableMap<Long, Session> = mutableMapOf()

    override fun getSession(update: Update): Session {
        val sessionId = extractSessionId(update)
        return getSession(sessionId)
    }

    override fun getSession(sessionId: Long): Session {
        return allSessions.computeIfAbsent(sessionId) {
            Session(
                sessionId,
                TransitionsHistory(procSpaceInit, historyCapacity),
                mutableMapOf()
            )
        }
    }

    override fun updateSession(session: Session) { }

    override fun getAllSessions(): List<Session> = allSessions.values.toList()

}