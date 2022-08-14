package com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api

import com.thorinhood.botFarm.engine.pagination.ElementsResponse
import com.thorinhood.botFarm.configs.YandexDiskConfig
import com.thorinhood.botFarm.fileStorageBot.extensions.FileStorageService
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class YandexDisk(
    private val restTemplate: RestTemplate,
    private val yandexDiskConfig: YandexDiskConfig
) : FileStorageService<YandexEntity> {

    override fun getAuthLink(): String =
        restTemplate.getForEntity<String>(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/authorization"
        ).body!!

    override fun getToken(code: String): String {
        return restTemplate.postForEntity(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/authorization",
            HttpEntity<String>("{ \"code\": ${code}}"),
            AccessToken::class.java
        ).body!!.access_token
    }

    override fun getEntities(token: String, path: String, offset: Int, limit: Int): ElementsResponse<YandexEntity> {
        val result = restTemplate.getForEntity<ElementsResponse<Map<String, Any>>>(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/content?" +
                    "path=$path&token=$token&offset=$offset&limit=$limit"
        ).body!!
        val entities = result.entities.map { value ->
            val name = value["name"] as String
            val type = value["type"] as String
            val href = value["href"] as String?
            YandexEntity(name, type, href)
        }
        return ElementsResponse(entities, result.limit, result.offset, result.total, result.path)
    }

    override fun createFolder(token: String, path: String): Boolean {
        return restTemplate.execute(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/folder?" +
                    "path=$path&token=$token", HttpMethod.PUT, {}, { response ->
                response.statusCode.is2xxSuccessful }) ?: false
    }

    override fun deleteEntity(token: String, path: String, permanently: Boolean): Boolean {
        return restTemplate.execute(
            "http://${yandexDiskConfig.host}:${yandexDiskConfig.port}/api/content?" +
                    "path=$path&token=$token&permanently=$permanently", HttpMethod.DELETE, {}, { response ->
                response.statusCode.is2xxSuccessful }) ?: false
    }

    data class AccessToken(val access_token: String)

}