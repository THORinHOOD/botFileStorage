package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class ChangeRadiusStartMessageProcessor : CommandProcessor {

    override fun getCommandName(): String = "\uD83C\uDF10 Изменить радиус поиска"

    override fun process(session: Session, parameter: String): List<BaseRequest<*, *>> =
        listOf(SendMessage(session.chatId, """
                                100 метров /radius_100
                                250 метров /radius_250
                                500 метров /radius_500
                                1000 метров /radius_1000
                                5000 метров /radius_5000
                            """.trimIndent()))
}