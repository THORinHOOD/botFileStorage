package com.thorinhood.botFarm.engine.sessions

interface Session<ID> {
    val sessionId: ID
    var cursor: com.thorinhood.botFarm.engine.sessions.Cursor
    val args: MutableMap<String, Any>
}