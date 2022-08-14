package com.thorinhood.botFarm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FileStorageBotApplication

fun main(args: Array<String>) {
	runApplication<FileStorageBotApplication>(*args)
}
