package com.thorinhood.fileStorageBot.data

data class EntitiesListResponse(
    val entities: List<Entity>,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val path: String
)
