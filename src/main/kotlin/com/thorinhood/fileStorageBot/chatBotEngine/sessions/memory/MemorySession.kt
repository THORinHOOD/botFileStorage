package com.thorinhood.fileStorageBot.chatBotEngine.sessions.memory

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Cursor
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

class MemorySession<ID>(
    override val sessionId: ID,
    override var cursor: Cursor,
    override val args: MutableMap<String, Any>
) : Session<ID>