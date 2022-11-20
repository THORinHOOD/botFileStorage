package com.thorinhood.botFarm.engine.sessions.memory

import com.thorinhood.botFarm.engine.sessions.Session

class MemorySession<ID>(
    override val sessionId: ID,
    override var procSpace: String,
    override val args: MutableMap<String, Any>
) : Session<ID>