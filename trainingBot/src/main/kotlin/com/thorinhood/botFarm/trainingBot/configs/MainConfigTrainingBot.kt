package com.thorinhood.botFarm.trainingBot.configs

import com.thorinhood.botFarm.trainingBot.services.GoogleTableService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class MainConfigTrainingBot(
    @Value("\${google.table.api.key}") val googleTableApiKey: String
) {

    @Bean
    fun googleTableService(
        restTemplate: RestTemplate
    ): GoogleTableService =
        GoogleTableService(
            restTemplate,
            googleTableApiKey
        )

}