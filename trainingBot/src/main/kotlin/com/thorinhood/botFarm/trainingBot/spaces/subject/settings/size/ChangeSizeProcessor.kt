package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.size

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class ChangeSizeProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> =
        message.value.message()?.text()?.toIntOrNull()?.let { newSize ->
            sessionArgumentsDataService.maintainWrap(message.getSessionId()) { sessionArguments ->
                val subjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
                subjects[sessionArguments[ArgKey.SELECTED_SUBJECT]]!!.lessonSize = newSize
            }
            transitionsHistoryConfigured.makeTransition(ProcSpace.IN_SUBJECT)
            listOf(
                SendMessage(
                    message.getSessionId(),
                    "Теперь в каждом занятии будет $newSize вопросов"
                ).replyMarkup(KeyboardMarkups.SUBJECT_KEYBOARD)
            )
        } ?: listOf(
            SendMessage(
                message.getSessionId(),
                "Странное сообщение ты мне прислал, пришли, что я тебя попросил ранее"
            )
        )
}