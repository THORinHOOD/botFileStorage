package com.benchinc.benchBot.configs

import de.westnordost.osmapi.OsmConnection
import de.westnordost.osmapi.overpass.OverpassMapDataApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OsmConfig(
    @Value("\${osm.api.url}") private val osmApiUrl : String,
    @Value("\${osm.user.agent}") private val osmUserAgent : String
) {

    @Bean
    fun osmConnection() : OsmConnection {
        return OsmConnection(osmApiUrl, osmUserAgent)
    }

    @Bean
    fun overpassMapDataApi(osmConnection: OsmConnection) : OverpassMapDataApi {
        return OverpassMapDataApi(osmConnection)
    }

}