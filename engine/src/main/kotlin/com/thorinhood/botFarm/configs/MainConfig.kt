package com.thorinhood.botFarm.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.data.services.base.DataService
import com.thorinhood.botFarm.engine.data.services.base.MemoryDataService
import com.thorinhood.botFarm.engine.data.services.base.MongoDataService
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class MainConfig(
    @Value("\${engine.procSpace.init}") val defaultProcSpace: String,
    @Value("\${engine.procSpace.history.capacity}") val historyCapacity: Int
) {

    @Bean
    fun engineObjectMapper() = ObjectMapper().registerModule(KotlinModule())

    @Profile("memory")
    @Primary
    @Bean
    fun memoryDataService() = MemoryDataService()

    @Profile("mongo")
    @Primary
    @Bean
    fun mongoDataService(mongoTemplate: MongoTemplate) = MongoDataService(mongoTemplate)

    @Bean
    fun transitionsHistoryDataService(dataService: DataService) =
        TransactionsHistoryDataService(dataService, defaultProcSpace, historyCapacity)

    @Bean
    fun sessionArgumentsDataService(dataService: DataService) =
        SessionArgumentsDataService(dataService)

    @Bean
    fun restTemplate() : RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(100))
        .build()

}