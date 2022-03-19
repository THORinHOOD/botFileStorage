package com.thorinhood.fileStorageBot.data

class AllSessions {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()

    fun getSession(chatId : Long) : Session =
        sessions.getOrPut(chatId) {
            Session(
                chatId,
                null,
                PipelineInfo("default", "?"),
                FileTreeInfo(
                    "disk:/",
                    mutableMapOf(),
                    0,
                    10
                ),
                null
            )
        }

}