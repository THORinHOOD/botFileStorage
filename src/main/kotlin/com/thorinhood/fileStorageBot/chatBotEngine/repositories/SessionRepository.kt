package com.thorinhood.fileStorageBot.chatBotEngine.repositories

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository : MongoRepository<Session, Long> {

}