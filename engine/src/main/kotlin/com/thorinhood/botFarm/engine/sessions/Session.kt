@file:Suppress("UNCHECKED_CAST")

package com.thorinhood.botFarm.engine.sessions

interface Session {
    val sessionId: Long
    val transitionsHistory: TransitionsHistory
    val args: MutableMap<String, Any>
    val procSpace: String
        get() = transitionsHistory.currentProcSpace()


    fun containsKey(key: String) : Boolean = args.containsKey(key)

    fun remove(key: String) {
        args.remove(key)
    }

    fun <ARG> remove(key: String) : ARG? =
        args.remove(key) as ARG?

    operator fun <ARG> get(key: String) : ARG =
        args[key] as ARG

    operator fun set(key: String, arg: Any) {
        args[key] = arg
    }

    fun <ARG> getOrPut(key: String, defaultValue: () -> ARG) : ARG =
        args.getOrPut(key) { defaultValue() as Any } as ARG
}