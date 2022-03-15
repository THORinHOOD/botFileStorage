package com.thorinhood.fileStorageBot.services.bot.pagination

data class PageCallback(
    val page: Int,
    val pageSize: Int
) {
    fun buildCallbackInfo(): String {
        return "${page}_${pageSize}"
    }

    companion object {
        fun fromList(data: List<String>): PageCallback =
            PageCallback(
                data[1].toInt(),
                data[2].toInt()
            )
    }
}