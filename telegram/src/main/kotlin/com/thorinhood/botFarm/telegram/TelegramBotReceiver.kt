package com.thorinhood.botFarm.telegram

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetUpdates
import com.thorinhood.botFarm.engine.messages.BotReceiver
import com.thorinhood.botFarm.engine.messages.Converter

class TelegramBotReceiver<IR>(
    private val telegramBot: TelegramBot,
    override val converter: Converter<Update, IR>
): BotReceiver<Update, IR>, UpdatesListener {

    private var process: ((IR) -> Unit)? = null

    override fun process(updates: MutableList<Update>?): Int {
        updates?.map { update ->
            converter.convert(update)
        }?.forEach { message ->
            process?.invoke(message)
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    override fun subscribe(process: (IR) -> Unit) {
        this.process = process
    }

    override fun start() {
        telegramBot.setUpdatesListener(this, GetUpdates().limit(3))
    }

}