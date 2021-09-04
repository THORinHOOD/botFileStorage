package com.benchinc.benchBot

import com.db.benchLib.configs.BenchLibClientsConfig
import com.db.benchLib.configs.BenchLibUtilsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [BenchLibClientsConfig::class, BenchLibUtilsConfig::class])
class BenchBotApplication

fun main(args: Array<String>) {
	runApplication<BenchBotApplication>(*args)
}
