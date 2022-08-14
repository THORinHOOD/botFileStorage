package com.thorinhood.botFarm.engine.sessions

class Cursor(
    var procSpace: String = "default",
    val args: MutableMap<String, Any> = mutableMapOf()
)