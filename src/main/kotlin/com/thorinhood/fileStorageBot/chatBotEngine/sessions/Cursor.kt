package com.thorinhood.fileStorageBot.chatBotEngine.sessions

class Cursor(
    var procSpace: String = "default",
    val args: MutableMap<String, Any> = mutableMapOf()
)