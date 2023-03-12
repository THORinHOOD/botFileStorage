package com.thorinhood.botFarm.telegram

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.messages.HasSessionId
import com.thorinhood.botFarm.engine.messages.HasText

typealias TelegramSendMessage = BaseRequest<*, *>
typealias TelegramReceiveMessage = Update

class TelegramReceiveMessageWrapper(
    val value: TelegramReceiveMessage
) : HasSessionId, HasText {
    override fun getSessionId(): Long =
        value.extractSessionId()

    override fun getText(): String =
        value.message()?.text() ?: ""
}

fun Update.extractSessionId() =
    message()?.chat()?.id() ?:
    callbackQuery()?.message()?.chat()?.id() ?:
    editedMessage()?.chat()?.id() ?:
    throw IllegalArgumentException("Can't find chat id in update")