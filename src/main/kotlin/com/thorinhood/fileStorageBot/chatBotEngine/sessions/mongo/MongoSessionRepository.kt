package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.data.mongodb.repository.MongoRepository

@ConditionalOnBean(MongoDbConfig::class)
interface MongoSessionRepository : MongoRepository<MongoSession<Long>, Long>