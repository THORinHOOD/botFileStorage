package com.thorinhood.botFarm.trainingBot.statics

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup

class KeyboardMarkups {
    companion object {
        val DEFAULT_KEYBOARD = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton("Начать занятие")),
            arrayOf(KeyboardButton("Изменить интервал выдачи заданий")),
            arrayOf(KeyboardButton("Изменить кол-во заданий в выдаче"))
        )
        val LESSON_KEYBOARD = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton("Не знаю")),
            arrayOf(KeyboardButton("Закончить занятие"))
        )
        val CANCEL_KEYBOARD = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton("Отмена"))
        )
    }
}