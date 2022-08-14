package com.thorinhood.botFarm.engine.pagination

data class PaginationContext<T>(
    val context: MutableMap<String, Any>,
    var offset: Int = 0,
    var limit: Int = 10,
    val elementsMap: MutableMap<String, T> = mutableMapOf()
)