package com.thorinhood.fileStorageBot

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.context.TypeExcludeFilter
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(
	exclude = [MongoAutoConfiguration::class]
)
//@EnableMongoRepositories
//@Import(value = [BenchLibClientsConfig::class, BenchLibUtilsConfig::class])
class FileStorageBotApplication

fun main(args: Array<String>) {
	runApplication<FileStorageBotApplication>(*args)
}
