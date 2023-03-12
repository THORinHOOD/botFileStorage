package com.thorinhood.botFarm.trainingBot.spaces

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import java.util.function.Predicate

class WelcomeProcessor : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistory: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        return listOf(SendMessage(message.getSessionId(), "Привет! ${Emojis.HAND_WAVE}")
            .replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD))
    }
}