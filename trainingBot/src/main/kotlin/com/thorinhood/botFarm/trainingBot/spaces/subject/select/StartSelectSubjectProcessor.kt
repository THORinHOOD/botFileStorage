package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class StartSelectSubjectProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistory: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val buttons = sessionArgumentsDataService.maintainWrap(message.getSessionId()) { sessionArguments ->
            val subjects = sessionArguments.getOrPut(ArgKey.SUBJECTS) { mutableMapOf<String, Subject>() }
            subjects.keys.map { subject ->
                arrayOf(KeyboardButton(subject))
            }.toMutableList()
        }
        if (buttons.isEmpty()) {
            return listOf(SendMessage(message.getSessionId(), "Не найдено ни одного предмета")
                .replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD))
        }
        buttons.add(arrayOf(KeyboardButton("Отмена")))
        transitionsHistory.makeTransition(ProcSpace.SELECT_SUBJECT)
        return listOf(SendMessage(message.getSessionId(), "Выбери один из предметов")
            .replyMarkup(ReplyKeyboardMarkup(*buttons.toTypedArray())))
    }
}