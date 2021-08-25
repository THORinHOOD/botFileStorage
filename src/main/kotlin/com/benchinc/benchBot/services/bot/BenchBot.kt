package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.services.bot.processors.ProcessorsService
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class BenchBot(private val telegramBot: TelegramBot,
               private val processorsService: ProcessorsService
) : UpdatesListener, Logging {

    val sessions : AllSessions = AllSessions()

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            try {
                processCommands(update)
                processCallbacks(update)
                processLocation(update)
            } catch (e:Exception) {
                logger.error(e)
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    private fun processCommands(update: Update) {
        update.message()?.text()?.let { text ->
            update.message().chat().id().let { chatId ->
                processorsService.processCommand(sessions.getSession(chatId), update.message().messageId(), text)
                    .forEach { telegramBot.execute(it) }
            }
        }
    }

    private fun processLocation(update: Update) {
        update.message()?.location()?.let { location ->
            update.message().chat().id().let { chatId ->
                processorsService.processLocation(sessions.getSession(chatId), location)
                    .forEach { telegramBot.execute(it) }
            }
        }
    }

    private fun processCallbacks(update: Update) {
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.message()?.chat()?.id()?.let { chatId ->
                processorsService.processCallback(sessions.getSession(chatId), callbackQuery)
                    .forEach { telegramBot.execute(it) }
            }
        }
    }

}