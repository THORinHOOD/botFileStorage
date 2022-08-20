package com.thorinhood.botFarm.engine

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.processors.ProcessorsManager
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class ChatBot(private val telegramBot: TelegramBot,
              private val processorsManager: ProcessorsManager
) : UpdatesListener, Logging {

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            processorsManager.process(update).forEach { telegramBot.execute(it) }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    fun sendMessage(message: BaseRequest<*, *>) {
        telegramBot.execute(message)
    }

    fun sendMessages(messages: List<BaseRequest<*, *>>) {
        messages.forEach { telegramBot.execute(it) }
    }

}