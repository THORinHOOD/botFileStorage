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

    override fun getSession(update: Update) : Session<Long> {
        val sessionId = extractSessionId(update)
        return getSession(sessionId)
    }

    override fun getSession(sessionId: Long): Session<Long> {
        return mongoSessionRepository.findById(sessionId).orElseGet {
            mongoSessionRepository.save(
                MongoSession(
                    sessionId,
                    TransitionsHistory(cursorProcSpaceInit),
                    mutableMapOf()
                )
            )
        }
    }

    override fun updateSession(session: Session<Long>) {
        if (session is MongoSession<Long>) {
            mongoSessionRepository.save(session)
        } else {
            throw IllegalArgumentException("You work with mongo, but session isn't MongoSession type")
        }
    }

    override fun getAllSessions(): List<Session<Long>> = mongoSessionRepository.findAll()

}