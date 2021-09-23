package com.geokittens.benchBot.services.bot.util

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
}