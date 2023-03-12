package com.thorinhood.botFarm.trainingBot.domain

import com.thorinhood.botFarm.engine.messages.Converter
import com.thorinhood.botFarm.telegram.TelegramSendMessage

class TelegramSenderConverter: Converter<TelegramSendMessage, TelegramSendMessage> {
    override fun convert(message: TelegramSendMessage) = message

}