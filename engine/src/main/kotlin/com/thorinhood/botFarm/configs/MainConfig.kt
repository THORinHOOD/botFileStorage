package com.thorinhood.botFarm.configs

import com.thorinhood.botFarm.engine.sessions.memory.MemorySessionsService
import com.thorinhood.botFarm.engine.sessions.mongo.MongoSessionRepository
import com.thorinhood.botFarm.engine.sessions.mongo.MongoSessionsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class MainConfig(
    @Value("\${engine.procSpace.init}") val procSpaceInit: String,
    @Value("\${engine.procSpace.history.capacity}") val historyCapacity: Int
) {

    @Profile("mongo")
    @Bean
    fun mongoSessionsService(mongoSessionRepository: MongoSessionRepository) : MongoSessionsService =
        MongoSessionsService(
            mongoSessionRepository,
            procSpaceInit,
            historyCapacity
        )

    @Profile("memory")
    @Bean
    fun memorySessionsService() : MemorySessionsService =
        MemorySessionsService(procSpaceInit, historyCapacity)

    @Bean
    fun restTemplate() : RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(100))
        .build()

}