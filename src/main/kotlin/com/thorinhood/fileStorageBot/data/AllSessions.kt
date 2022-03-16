package com.thorinhood.fileStorageBot.data

class AllSessions {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()

    fun getSession(chatId : Long) : Session =
        sessions.getOrPut(chatId) {
            Session(chatId, null,  "disk:/", PipelineInfo("default", "?"), mutableMapOf())
        }

}