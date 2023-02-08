@file:Suppress("UNCHECKED_CAST")

package com.thorinhood.botFarm.engine.data.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("session_arguments")
class SessionArguments(
    @Id override val sessionId: Long,
    private val args: MutableMap<String, Any>
) : Entity {

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