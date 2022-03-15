package com.thorinhood.fileStorageBot.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class MainConfig(
    @Value("\${telegram.token}") private val telegramBotToken : String
) {

    @Bean
    fun restTemplate() : RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofSeconds(100))
        .build()

}