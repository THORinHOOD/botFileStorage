package com.thorinhood.botFarm.engine.sessions.mongo

import com.pengrad.telegrambot.model.Update

import com.thorinhood.botFarm.engine.sessions.Cursor
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("mongo")
@Service
class MongoSessionsService(
    private val mongoSessionRepository: com.thorinhood.botFarm.engine.sessions.mongo.MongoSessionRepository
) : SessionsService {

    override fun getSession(update: Update) : Session<Long> {
        val chatId = extractSessionId(update)
        return mongoSessionRepository.findById(chatId).orElseGet {
            mongoSessionRepository.save(
                com.thorinhood.botFarm.engine.sessions.mongo.MongoSession(
                    chatId,
                    com.thorinhood.botFarm.engine.sessions.Cursor(),
                    mutableMapOf()
                )
            )
        }
    }

    override fun updateSession(session: Session<Long>) {
        if (session is com.thorinhood.botFarm.engine.sessions.mongo.MongoSession<Long>) {
            mongoSessionRepository.save(session)
        } else {
            throw IllegalArgumentException("You work with mongo, but session isn't MongoSession type")
        }
    }

}