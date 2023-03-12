package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class SelectSubjectProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val subjectName = message.value.message().text()
        return sessionArgumentsDataService.maintainWrap(message.getSessionId()) { args ->
            if (!args.containsKey(ArgKey.SUBJECTS)) {
                transitionsHistoryConfigured.makeTransition(ProcSpace.DEFAULT)
                return@maintainWrap listOf(
                    SendMessage(
                        message.getSessionId(),
                        "${Emojis.HMM}\nНе найдено ни одного предмета",
                    ).replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD)
                )
            }
            val subjects = args.get<AllSubjects>(ArgKey.SUBJECTS)
            if (!subjects.containsKey(subjectName)) {
                return@maintainWrap listOf(SendMessage(message.getSessionId(), "Такого предмета нет, попробуй снова"))
            }
            args[ArgKey.SELECTED_SUBJECT] = subjectName
            transitionsHistoryConfigured.makeTransition(ProcSpace.IN_SUBJECT)
            return@maintainWrap listOf(
                SendMessage(
                    message.getSessionId(),
                    "Что будем делать с этим предметом?"
                ).replyMarkup(KeyboardMarkups.SUBJECT_KEYBOARD)
            )
        }
    }
}