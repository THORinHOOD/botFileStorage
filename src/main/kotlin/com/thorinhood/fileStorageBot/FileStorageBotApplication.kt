package com.thorinhood.fileStorageBot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [MongoAutoConfiguration::class]
)
class FileStorageBotApplication

fun main(args: Array<String>) {
	runApplication<FileStorageBotApplication>(*args)
}
