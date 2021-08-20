package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ChangeRadiusProcessor : CommandProcessor {

    override fun getCommandName(): String = "radius"

    override fun process(session: Session, text: String) : AbstractSendRequest<*> {
        session.radius =  text.substring(8).toInt()
        return SendMessage(session.chatId, "Радиус поиска изменён на ${session.radius} метров вокруг")
    }

}