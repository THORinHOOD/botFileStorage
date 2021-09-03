package com.benchinc.benchBot.configs

import com.benchinc.benchBot.configs.clients.BenchesServiceClient
import feign.Feign
import feign.Logger
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientsConfig(
    @Value("\${benchesService.host}") val benchesServiceHost: String,
    @Value("\${benchesService.port}") val benchesServicePort: Int
) {

    @Bean
    fun benchesServiceClient() : BenchesServiceClient {
        val url = "http://${benchesServiceHost}:${benchesServicePort}/api/benches"
        return Feign.builder()
            .client(OkHttpClient())
            .encoder(GsonEncoder())
            .decoder(GsonDecoder())
            .logger(Slf4jLogger(BenchesServiceClient::class.java))
            .logLevel(Logger.Level.FULL)
            .target(BenchesServiceClient::class.java, url)
    }

}