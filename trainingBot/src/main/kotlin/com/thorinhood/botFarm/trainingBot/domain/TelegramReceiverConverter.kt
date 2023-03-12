package com.thorinhood.botFarm.trainingBot.domain

import com.thorinhood.botFarm.engine.messages.Converter
import com.thorinhood.botFarm.telegram.TelegramReceiveMessage
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.extractSessionId

class TelegramReceiverConverter: Converter<TelegramReceiveMessage, TelegramReceiveMessageWrapper> {
    override fun convert(message: TelegramReceiveMessage): TelegramReceiveMessageWrapper =
        TelegramReceiveMessageWrapper(message)

}