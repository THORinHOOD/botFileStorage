package com.benchinc.benchBot

import com.db.benchLib.configs.ClientsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(ClientsConfig::class)
class BenchBotApplication

fun main(args: Array<String>) {
	runApplication<BenchBotApplication>(*args)
}
