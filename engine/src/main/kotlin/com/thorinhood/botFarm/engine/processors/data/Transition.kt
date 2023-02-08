package com.thorinhood.botFarm.engine.processors.data

import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage


abstract class BaseTransition(
    private val message: String? = null,
    private val keyboard: Keyboard? = null,
    val postTransitionAction: ((Session) -> Unit)? = null
) {
    fun makeMessage(chatId: Long) : SendMessage? =
        message?.let {
            val message = SendMessage(chatId, it).parseMode(ParseMode.HTML)
            keyboard?.let { message.replyMarkup(it) }
            message
        }
}

class Transition(
    val procSpace: String,
    message: String? = null,
    keyboard: Keyboard? = null,
    postTransitionAction: ((Session) -> Unit)? = null
) : BaseTransition(message, keyboard, postTransitionAction)

class BackTransition(
    message: String? = null,
    keyboard: Keyboard? = null,
    postTransitionAction: ((Session) -> Unit)? = null
) : BaseTransition(message, keyboard, postTransitionAction)