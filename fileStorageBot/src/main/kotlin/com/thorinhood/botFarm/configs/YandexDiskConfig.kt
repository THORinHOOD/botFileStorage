package com.thorinhood.botFarm.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class YandexDiskConfig(
    @Value("\${yandexdisk.host}") val host: String,
    @Value("\${yandexdisk.port}") val port: Int
)