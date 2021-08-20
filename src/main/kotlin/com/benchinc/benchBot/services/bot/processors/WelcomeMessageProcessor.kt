package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class WelcomeMessageProcessor : CommandProcessor {

    override fun getCommandName(): String = "start"

    override fun process(session: Session, text: String): AbstractSendRequest<*> =
        SendMessage(session.chatId, "Привет")
            .parseMode(ParseMode.HTML)
            .replyMarkup(
                ReplyKeyboardMarkup(
                arrayOf(
                    KeyboardButton("\uD83D\uDCCD Поиск лавочек")
                    .requestLocation(true)),
                arrayOf(KeyboardButton("\uD83C\uDF10 Изменить радиус поиска"))
            ).resizeKeyboard(true))

}