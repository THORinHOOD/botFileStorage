package com.thorinhood.botFarm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TrainingBotApplication

fun main(args: Array<String>) {
    runApplication<TrainingBotApplication>(*args)
}