package com.thorinhood.botFarm.engine.sessions.mongo

import org.apache.logging.log4j.kotlin.Logging
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Profile("mongo")
@Configuration
@EnableMongoRepositories
class MongoDbConfig : Logging

@Profile("!mongo")
@Configuration
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration::class])
class NotMongoDbConfig