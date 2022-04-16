package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.repository.MongoRepository

@Profile("mongo")
interface MongoSessionRepository : MongoRepository<MongoSession<Long>, Long>