package com.thorinhood.botFarm.engine.sessions

interface Session<ID> {
    val sessionId: ID
    var procSpace: String
    val args: MutableMap<String, Any>

    fun hasArg(argKey: String) : Boolean
}