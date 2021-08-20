package com.benchinc.benchBot.configs

import com.pengrad.telegrambot.TelegramBot
import de.westnordost.osmapi.OsmConnection
import de.westnordost.osmapi.overpass.OverpassMapDataApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MainConfig {

    @Value("\${telegram.token}")
    lateinit var telegramBotToken : String

    @Value("\${osm.api.url}")
    lateinit var osmApiUrl : String

    @Value("\${osm.user.agent}")
    lateinit var osmUserAgent : String

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

}