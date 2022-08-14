package com.thorinhood.botFarm.engine.pagination

data class ElementsResponse<T>(
    val entities: List<T>,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val path: String
)