package com.thorinhood.botFarm.engine.sessions.memory

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.TransitionsHistory

class MemorySession(
    override val sessionId: Long,
    override val transitionsHistory: TransitionsHistory,
    override val args: MutableMap<String, Any>
) : Session