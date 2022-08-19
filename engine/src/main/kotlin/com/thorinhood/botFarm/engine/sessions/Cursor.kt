package com.thorinhood.botFarm.engine.sessions

class Cursor(
    var procSpace: String,
    val args: MutableMap<String, Any> = mutableMapOf()
)