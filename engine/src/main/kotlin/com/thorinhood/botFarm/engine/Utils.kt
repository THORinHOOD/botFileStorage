package com.thorinhood.botFarm.engine

import com.pengrad.telegrambot.model.Update

fun Update.extractSessionId() =
    message()?.chat()?.id() ?:
    callbackQuery()?.message()?.chat()?.id() ?:
    editedMessage()?.chat()?.id() ?:
    throw IllegalArgumentException("Can't find chat id in update")