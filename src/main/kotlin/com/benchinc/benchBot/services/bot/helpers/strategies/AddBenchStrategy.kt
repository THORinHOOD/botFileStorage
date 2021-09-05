package com.benchinc.benchBot.services.bot.helpers.strategies

interface AddBenchStrategy {
    fun commitRequest(chatId: Long, photo: ByteArray) : String
}