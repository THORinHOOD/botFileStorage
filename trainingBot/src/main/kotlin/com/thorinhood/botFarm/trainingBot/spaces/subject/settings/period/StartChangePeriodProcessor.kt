package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period

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

class StartChangePeriodProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val sessionArguments = sessionArgumentsDataService.getBySessionId(message.getSessionId())
        val subjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
        val subject = subjects[sessionArguments[ArgKey.SELECTED_SUBJECT]]!!
        val milliseconds = subject.scheduleConfig.period
        transitionsHistoryConfigured.makeTransition(ProcSpace.CHANGE_PERIOD)

        return listOf(
            SendMessage(
                message.getSessionId(), "Напиши, как часто надо приходить к тебе с заданиями (каждые N-минут).\n" +
                        "На данный момент я прихожу к тебе " +
                        "каждые ${milliseconds / 1000 / 60} минут"
            ).replyMarkup(KeyboardMarkups.CANCEL_KEYBOARD)
        )
    }
}