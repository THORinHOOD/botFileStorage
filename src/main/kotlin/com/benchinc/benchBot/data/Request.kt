package com.benchinc.benchBot.data

import com.pengrad.telegrambot.model.Location

class Request(
    var chatId: Long,
    var photo: ByteArray?,
    var location: Location
)