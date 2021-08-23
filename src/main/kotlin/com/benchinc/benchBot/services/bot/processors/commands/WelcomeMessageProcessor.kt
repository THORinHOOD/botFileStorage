package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class WelcomeMessageProcessor : CommandProcessor {

    override val commandName: String = "start"

    override fun process(session: Session, parameter: String): List<BaseRequest<*, *>> =
        listOf(SendMessage(session.chatId, "Привет")
            .parseMode(ParseMode.HTML)
            .replyMarkup(
                ReplyKeyboardMarkup(
                arrayOf(
                    KeyboardButton("\uD83D\uDCCD Поиск лавочек")
                    .requestLocation(true)),
                arrayOf(KeyboardButton("\uD83C\uDF10 Изменить радиус поиска"))
            ).resizeKeyboard(true)))

}