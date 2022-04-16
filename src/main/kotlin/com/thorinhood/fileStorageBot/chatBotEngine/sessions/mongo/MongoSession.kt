package com.thorinhood.fileStorageBot.chatBotEngine.sessions.mongo

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Cursor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sessions")
class MongoSession<ID>(
    @Id override val sessionId: ID,
    override var cursor: Cursor,
    override val args: MutableMap<String, Any>) : Session<ID>