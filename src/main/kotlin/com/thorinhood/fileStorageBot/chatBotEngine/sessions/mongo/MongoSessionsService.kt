package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import com.pengrad.telegrambot.model.Update

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Cursor
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.SessionsService

class MongoSessionsService(
    private val mongoSessionRepository: MongoSessionRepository
) : SessionsService {

    override fun getSession(update: Update) : Session<Long> {
        val chatId = extractSessionId(update)
        return mongoSessionRepository.findById(chatId).orElseGet {
            mongoSessionRepository.save(
                MongoSession(
                    chatId,
                    Cursor(),
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

}