package com.thorinhood.fileStorageBot.data

import com.thorinhood.fileStorageBot.services.api.YandexEntity

data class EntitiesListResponse(
    val entities: List<YandexEntity>,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val path: String
)
