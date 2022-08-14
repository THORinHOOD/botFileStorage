package com.thorinhood.botFarm.engine.sessions.mongo

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.Cursor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sessions")
class MongoSession<ID>(
    @Id override val sessionId: ID,
    override var cursor: com.thorinhood.botFarm.engine.sessions.Cursor,
    override val args: MutableMap<String, Any>) : Session<ID>