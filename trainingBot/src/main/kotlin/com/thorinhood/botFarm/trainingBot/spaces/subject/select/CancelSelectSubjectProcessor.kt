package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class CancelSelectSubjectProcessor : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var procSpace: String = ""
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        transitionsHistoryConfigured.makeTransition(ProcSpace.DEFAULT)
        return listOf(
            SendMessage(
                message.getSessionId(),
                "Окей, не будем"
            ).replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD)
        )
    }
}