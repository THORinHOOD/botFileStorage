package com.geokittens.benchBot.services.bot.pagination

data class PageCallback(
    val page: Int,
    val pageSize: Int,
    val lat: Double,
    val lon: Double,
    val radius: Double
) {
    fun buildCallbackInfo(): String {
        return "${page}_${pageSize}_${lat}_${lon}_${radius}"
    }

    companion object {
        fun fromList(data: List<String>): PageCallback =
            PageCallback(
                data[1].toInt(),
                data[2].toInt(),
                data[3].toDouble(),
                data[4].toDouble(),
                data[5].toDouble()
            )
    }
}