package com.thorinhood.botFarm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestBotApplication

fun main(args: Array<String>) {
    runApplication<TestBotApplication>(*args)
}