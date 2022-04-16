package com.thorinhood.fileStorageBot.chatBotEngine.sessions.memory

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.Utils
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Cursor
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.SessionsService
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo.MongoDbConfig
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.stereotype.Service

@Service
@ConditionalOnMissingBean(
    value = [MongoDbConfig::class]
)
class MemorySessionsService : SessionsService, Logging {

    private val allSessions: MutableMap<Long, MemorySession<Long>> = mutableMapOf()

    init {
        Utils.initLog(logger, "memory")
    }

    override fun getSession(update: Update): Session<Long> {
        val chatId = extractSessionId(update)
        return allSessions.computeIfAbsent(chatId) {
            MemorySession(
                chatId,
                Cursor(),
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
}