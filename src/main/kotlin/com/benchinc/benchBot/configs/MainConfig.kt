package com.benchinc.benchBot.configs

import com.pengrad.telegrambot.TelegramBot
import de.westnordost.osmapi.OsmConnection
import de.westnordost.osmapi.overpass.OverpassMapDataApi
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MainConfig(
    @Value("\${telegram.token}") private val telegramBotToken : String,
    @Value("\${osm.api.url}") private val osmApiUrl : String,
    @Value("\${osm.user.agent}") private val osmUserAgent : String
) {

    @Bean
    fun telegramBot() : TelegramBot {
        return TelegramBot(telegramBotToken)
    }

    @Bean
    fun osmConnection() : OsmConnection {
        return OsmConnection(osmApiUrl, osmUserAgent)
    }

    @Bean
    fun overpassMapDataApi(osmConnection: OsmConnection) : OverpassMapDataApi {
        return OverpassMapDataApi(osmConnection)
    }

    @Bean
    fun requestsCounter(collectorRegistry: CollectorRegistry) : Counter =
        Counter.build()
            .name("request_count")
            .labelNames("request_name")
            .help("Number of requests")
            .register(collectorRegistry)

}