package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import java.util.function.Predicate

class StartLessonProcessor(
    private val lessonService: LessonService,
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var defaultTransition: String? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> =
        sessionArgumentsDataService.maintainWrap(message.getSessionId()) { args ->
            listOf(
                SendMessage(
                    message.getSessionId(),
                    "Okaaaaay, let's go!"
                ).replyMarkup(KeyboardMarkups.LESSON_KEYBOARD),
            ) + lessonService.startLesson(message.getSessionId(), transitionsHistoryConfigured, args)
        }
}