package com.thorinhood.fileStorageBot.chatBotEngine.sessions

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.repositories.SessionRepository
import org.springframework.stereotype.Service

@Service
class SessionsService(
    private val sessionRepository: SessionRepository
) {

    fun getSession(update: Update) : Session {
        val chatId = extractChatId(update)
        return sessionRepository.findById(chatId).orElseGet {
            sessionRepository.save(createDefaultSession(chatId))
        }
    }

    fun updateSession(session: Session) {
        sessionRepository.save(session)
    }

    private fun extractChatId(update: Update) : Long =
        update.message()?.chat()?.id() ?:
        update.callbackQuery()?.message()?.chat()?.id() ?:
        update.editedMessage()?.chat()?.id() ?:
        throw IllegalArgumentException("Can't find chat id in update")

    private fun createDefaultSession(chatId : Long) : Session =
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