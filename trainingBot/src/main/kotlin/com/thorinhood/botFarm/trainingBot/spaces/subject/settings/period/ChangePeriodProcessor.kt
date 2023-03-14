package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class ChangePeriodProcessor(
    private val subjectService: SubjectService,
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {
    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var defaultTransition: String? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> =
        message.value.message()?.text()?.toLongOrNull()?.let { newPeriod ->
            sessionArgumentsDataService.maintainWrap(message.getSessionId()) { sessionArguments ->
                val subjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
                val subject = subjects[sessionArguments[ArgKey.SELECTED_SUBJECT]]!!
                subject.scheduleConfig.period = newPeriod * 60 * 1000
                subjectService.rescheduleSubject(message.getSessionId(), subject)
            }
            transitionsHistoryConfigured.makeTransition(ProcSpace.IN_SUBJECT)
            listOf(
                SendMessage(
                    message.getSessionId(),
                    "Поменял интервал на каждые $newPeriod минут"
                ).replyMarkup(KeyboardMarkups.SUBJECT_KEYBOARD)
            )
        } ?: listOf(
            SendMessage(
                message.getSessionId(),
                "Странное сообщение ты мне прислал, пришли, что я тебя попросил ранее"
            )
        )
}