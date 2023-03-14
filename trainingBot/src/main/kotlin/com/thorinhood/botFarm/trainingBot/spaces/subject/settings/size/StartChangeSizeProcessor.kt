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
import java.util.function.Predicate

class StartChangeSizeProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var defaultTransition: String? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val sessionArguments = sessionArgumentsDataService.getBySessionId(message.getSessionId())
        val subjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
        return listOf(
            SendMessage(
                message.getSessionId(),
                "Напиши, сколько вопросов должно быть в одном задании.\n" +
                        "На данный момент я прихожу к тебе с " +
                        "${subjects[sessionArguments[ArgKey.SELECTED_SUBJECT]]!!.lessonSize} вопросами"
            ).replyMarkup(KeyboardMarkups.CANCEL_KEYBOARD)
        )
    }
}