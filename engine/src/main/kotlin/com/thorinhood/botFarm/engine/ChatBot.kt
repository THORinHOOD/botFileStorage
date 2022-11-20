package com.thorinhood.botFarm.engine

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.configs.TelegramMessage
import com.thorinhood.botFarm.engine.messages.MsgSender
import com.thorinhood.botFarm.engine.processors.ProcessorsManager
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class ChatBot(private val msgSender: MsgSender<TelegramMessage>,
              private val processorsManager: ProcessorsManager,
              telegramBot: TelegramBot
) : UpdatesListener, Logging {

    init {
        telegramBot.setUpdatesListener(this)
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            msgSender.sendMessages(processorsManager.process(update))
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

}