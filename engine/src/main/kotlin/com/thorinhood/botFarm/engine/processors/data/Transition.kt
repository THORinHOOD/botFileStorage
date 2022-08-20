package com.thorinhood.botFarm.engine.processors.data

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.Session

class Transition(
    val procSpace: String,
    private val description: String? = null,
    private val replyKeyboardMarkup: ReplyKeyboardMarkup? = null,
    val preTransitionAction: ((Session<Long>) -> Unit)? = null,
) {
    fun makeMessage(chatId: Long) : SendMessage? =
        if (description == null) {
            null
        } else {
            val message = SendMessage(chatId, description).parseMode(ParseMode.HTML)
            if (replyKeyboardMarkup != null) {
                message.replyMarkup(replyKeyboardMarkup)
            }
            message
        }
}