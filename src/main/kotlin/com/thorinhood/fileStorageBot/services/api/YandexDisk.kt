package com.thorinhood.fileStorageBot.services.api

import com.thorinhood.fileStorageBot.configs.YandexDiskConfig
import com.thorinhood.fileStorageBot.data.EntitiesListResponse
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class YandexDisk(
    private val restTemplate: RestTemplate,
    private val yandexDiskConfig: YandexDiskConfig
) : FileStorageService {

    override fun getAuthLink(): String =
        restTemplate.getForEntity<String>(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/token-link"
        ).body!!

    override fun getToken(code: String): String {
        return restTemplate.postForEntity(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/code",
            HttpEntity<String>("{ \"code\": ${code}}"),
            AccessToken::class.java
        ).body!!.access_token
    }

    override fun getEntities(token: String, path: String, offset: Int, limit: Int): EntitiesListResponse {
        return restTemplate.getForEntity<EntitiesListResponse>(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/content?" +
                    "path=$path&token=$token&offset=$offset&limit=$limit"
        ).body!!
    }

    data class AccessToken(val access_token: String)

}