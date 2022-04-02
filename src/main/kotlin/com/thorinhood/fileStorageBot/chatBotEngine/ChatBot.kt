package com.thorinhood.fileStorageBot.chatBotEngine

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.ProcessorsManager
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.AllSessionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class ChatBot(private val telegramBot: TelegramBot,
              private val processorsManager: ProcessorsManager,
              private val allSessionsService: AllSessionsService
) : UpdatesListener, Logging {

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            processorsManager.process(allSessionsService.getSession(update), update)
                .forEach { telegramBot.execute(it) }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

}