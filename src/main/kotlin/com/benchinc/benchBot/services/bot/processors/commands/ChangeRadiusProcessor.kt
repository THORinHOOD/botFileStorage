package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ChangeRadiusProcessor : CommandProcessor {

    override fun getCommandName(): String = "radius"

    override fun process(session: Session, parameter: String) : List<BaseRequest<*, *>> {
        session.radius =  parameter.substring(8).toInt()
        return listOf(SendMessage(session.chatId, "Радиус поиска изменён на ${session.radius} метров вокруг"))
    }

}