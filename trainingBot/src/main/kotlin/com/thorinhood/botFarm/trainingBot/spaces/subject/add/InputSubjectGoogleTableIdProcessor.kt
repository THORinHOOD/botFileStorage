package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import java.util.function.Predicate

class InputSubjectGoogleTableIdProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val googleTableId = message.value.message()?.text() ?: throw Exception("Попробуй ещё раз")
        sessionArgumentsDataService.maintainWrap(message.getSessionId()) { args ->
            (args.get<Subject.Builder>(ArgKey.SUBJECT_BUILDER)).googleTableId(googleTableId)
        }
        transitionsHistoryConfigured.makeTransition(ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_SHEET)
        return listOf(
            SendMessage(
                message.getSessionId(),
                "Отлично!\nА теперь пришли названия листа в таблице"
            ).replyMarkup(KeyboardMarkups.CANCEL_KEYBOARD)
        )
    }
}