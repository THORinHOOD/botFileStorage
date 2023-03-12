package com.thorinhood.botFarm.engine.messages

interface BotSender<OS, IS> {
    val converter: Converter<IS, OS>
    fun sendMessages(messages: List<IS>)
    fun sendMessage(message: IS)
}