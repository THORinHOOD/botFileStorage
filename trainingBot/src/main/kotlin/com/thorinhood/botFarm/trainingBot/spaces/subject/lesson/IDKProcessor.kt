package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import java.util.function.Predicate

class IDKProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val args = sessionArgumentsDataService.getBySessionId(message.getSessionId())
        val lesson: Lesson = args[ArgKey.LESSON]
        val answer = (lesson.getCurrentTask().answers).first()
        val blank = answer.indices.random()
        val hint = answer.mapIndexed { i, symbol -> if (i == blank) "_" else symbol }.joinToString()
        return listOf(
            SendMessage(
                message.getSessionId(),
                "Подсказка : \n\"${hint}\""
            )
        )
    }
}