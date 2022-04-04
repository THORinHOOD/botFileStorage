package com.thorinhood.fileStorageBot.chatBotEngine.pagination

data class PaginationContext<T : Entity>(
    val context: MutableMap<String, Any>,
    var offset: Int = 0,
    var limit: Int = 10,
    val elementsMap: MutableMap<String, T> = mutableMapOf()
)