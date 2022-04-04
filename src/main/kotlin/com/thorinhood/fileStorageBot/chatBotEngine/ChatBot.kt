package com.thorinhood.fileStorageBot.chatBotEngine

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.ProcessorsManager
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.SessionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import java.io.PrintWriter
import java.io.StringWriter

@Service
class ChatBot(private val telegramBot: TelegramBot,
              private val processorsManager: ProcessorsManager,
              private val sessionsService: SessionsService
) : UpdatesListener, Logging {

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            val session = sessionsService.getSession(update)
            processorsManager.process(session, update)
                .forEach { telegramBot.execute(it) }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

}