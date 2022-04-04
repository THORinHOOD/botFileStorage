package com.thorinhood.fileStorageBot.chatBotEngine.pagination

data class ElementsResponse<T>(
    val entities: List<T>,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val path: String
)