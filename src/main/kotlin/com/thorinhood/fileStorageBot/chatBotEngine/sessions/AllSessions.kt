package com.thorinhood.fileStorageBot.chatBotEngine.sessions

class AllSessions {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()

    fun getSession(chatId : Long) : Session =
        sessions.getOrPut(chatId) {
            Session(
                chatId,
                null,
                Cursor(),
                FileTreeInfo(
                    "disk:/",
                    mutableMapOf(),
                    0,
                    10
                )
            )
        }

}