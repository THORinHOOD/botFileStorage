package com.thorinhood.fileStorageBot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
//@Import(value = [BenchLibClientsConfig::class, BenchLibUtilsConfig::class])
class FileStorageBotApplication

fun main(args: Array<String>) {
	runApplication<FileStorageBotApplication>(*args)
}
