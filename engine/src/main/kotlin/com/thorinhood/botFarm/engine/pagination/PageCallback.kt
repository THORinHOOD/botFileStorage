package com.thorinhood.botFarm.engine.pagination

data class PageCallback(
    val page: Int,
    val pageSize: Int
) {
    fun buildCallbackInfo(): String {
        return "${page}_${pageSize}"
    }

    companion object {
        fun fromList(data: List<String>): com.thorinhood.botFarm.engine.pagination.PageCallback =
            com.thorinhood.botFarm.engine.pagination.PageCallback(
                data[1].toInt(),
                data[2].toInt()
            )
    }
}