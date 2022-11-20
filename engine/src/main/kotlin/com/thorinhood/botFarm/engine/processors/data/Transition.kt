package com.thorinhood.botFarm.engine.processors.data

import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.Session

class Transition(
    val procSpace: String,
    private val message: String? = null,
    private val keyboard: Keyboard? = null,
    val preTransitionAction: ((Session<Long>) -> Unit)? = null,
) {
    fun makeMessage(chatId: Long) : SendMessage? =
        if (message == null) {
            null
        } else {
            val message = SendMessage(chatId, message).parseMode(ParseMode.HTML)
            keyboard?.let { message.replyMarkup(it) }
            message
        }
}