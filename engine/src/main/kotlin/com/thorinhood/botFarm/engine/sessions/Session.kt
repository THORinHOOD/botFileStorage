package com.thorinhood.botFarm.engine.sessions

interface Session<ID> {
    val sessionId: ID
    var cursor: Cursor
    val args: MutableMap<String, Any>
}