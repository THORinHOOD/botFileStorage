package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.services.bot.processors.managers.MainProcessorsManager
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class BenchBot(private val telegramBot: TelegramBot,
               private val mainProcessorsManager: MainProcessorsManager
) : UpdatesListener, Logging {

    val sessions : AllSessions = AllSessions()

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            try {
                mainProcessorsManager.process(sessions, update)
                    .forEach { telegramBot.execute(it) }
            } catch (e:Exception) {
                logger.error(e)
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

}