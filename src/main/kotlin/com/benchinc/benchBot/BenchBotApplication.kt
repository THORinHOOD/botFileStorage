package com.benchinc.benchBot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BenchBotApplication

fun main(args: Array<String>) {
	runApplication<BenchBotApplication>(*args)
}
