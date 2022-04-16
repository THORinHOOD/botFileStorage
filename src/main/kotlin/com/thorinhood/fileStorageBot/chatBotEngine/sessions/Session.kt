package com.thorinhood.fileStorageBot.chatBotEngine.sessions

interface Session<ID> {
    val sessionId: ID
    var cursor: Cursor
    val args: MutableMap<String, Any>
}