package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@ConditionalOnProperty(
    value = ["spring.data.mongodb.host",
             "spring.data.mongodb.port",
             "spring.data.mongodb.database",
             "spring.data.mongodb.username",
             "spring.data.mongodb.password"]
)
@EnableMongoRepositories
@Import(value = [MongoAutoConfiguration::class])
class MongoDbConfig {

    @Bean
    fun sessionsService(mongoSessionRepository: MongoSessionRepository)
        = MongoSessionsService(mongoSessionRepository)

}