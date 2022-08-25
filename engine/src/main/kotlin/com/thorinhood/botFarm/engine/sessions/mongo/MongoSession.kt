package com.thorinhood.botFarm.engine.sessions.mongo

import com.thorinhood.botFarm.engine.sessions.Session
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sessions")
class MongoSession<ID>(
    @Id override val sessionId: ID,
    override var procSpace: String,
    override val args: MutableMap<String, Any>) : Session<ID> {
    override fun hasArg(argKey: String): Boolean = args.containsKey(argKey)
}