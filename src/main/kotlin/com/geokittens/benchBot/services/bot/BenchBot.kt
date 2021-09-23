package com.geokittens.benchBot.services.bot

import com.geokittens.benchBot.services.AllSessionsService
import com.geokittens.benchBot.services.bot.pipelines.PipelinesManager
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class BenchBot(private val telegramBot: TelegramBot,
               private val pipelinesManager: PipelinesManager,
               private val allSessionsService: AllSessionsService
) : UpdatesListener, Logging {

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            try {
                pipelinesManager.process(allSessionsService.getSession(update), update)
                    .forEach { telegramBot.execute(it) }
            } catch (e:Exception) {
                logger.error(e)
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

}