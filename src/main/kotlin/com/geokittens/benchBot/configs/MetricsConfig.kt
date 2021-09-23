package com.geokittens.benchBot.configs

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsConfig {

    @Bean
    fun requestsCounter(collectorRegistry: CollectorRegistry) : Counter =
        Counter.build()
            .name("request_count")
            .labelNames("request_name")
            .help("Number of requests")
            .register(collectorRegistry)

}