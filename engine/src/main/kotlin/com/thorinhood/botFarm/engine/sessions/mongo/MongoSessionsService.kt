package com.thorinhood.botFarm.engine.sessions.mongo

import com.pengrad.telegrambot.model.Update

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.engine.sessions.TransitionsHistory

class MongoSessionsService(
    private val mongoSessionRepository: MongoSessionRepository,
    private val cursorProcSpaceInit: String,
    private val historyCapacity: Int
) : SessionsService {

    override fun getSession(update: Update) : Session {
        val sessionId = extractSessionId(update)
        return getSession(sessionId)
    }

    override fun getSession(sessionId: Long): Session {
        return mongoSessionRepository.findById(sessionId).orElseGet {
            mongoSessionRepository.save(
                MongoSession(
                    sessionId,
                    TransitionsHistory(cursorProcSpaceInit, historyCapacity),
                    mutableMapOf()
                )
            )
        }
    }

    override fun updateSession(session: Session) {
        if (session is MongoSession) {
            mongoSessionRepository.save(session)
        } else {
            throw IllegalArgumentException("You work with mongo, but session isn't MongoSession type")
        }
    }

    override fun getAllSessions(): List<Session> = mongoSessionRepository.findAll()

}