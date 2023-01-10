package com.thorinhood.botFarm.engine.sessions.mongo

import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.TransitionsHistory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sessions")
class MongoSession<ID>(
    @Id override val sessionId: ID,
    override val transitionsHistory: TransitionsHistory,
    override val args: MutableMap<String, Any>) : Session<ID>