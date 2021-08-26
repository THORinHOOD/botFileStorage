package com.benchinc.benchBot.data

class AllSessions {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()

    fun getSession(chatId : Long) : Session = sessions.getOrPut(chatId) { Session(chatId, listOf(), 500,
        PipelineInfo("default", "?")) }

}