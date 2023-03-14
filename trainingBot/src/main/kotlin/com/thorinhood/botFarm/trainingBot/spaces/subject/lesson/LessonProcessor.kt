package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class LessonProcessor(
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
            val lesson : Lesson = args[ArgKey.LESSON]
            if (lesson.getCurrentTask().answers.contains(message.value.message()?.text()?.lowercase())) {
                val previousTask = lesson.removeCurrentTask()
                if (lesson.hasTask()) {
                    val nextTaskMessage = lessonService.makeCurrentTaskMessage(message.getSessionId(), lesson)
                    listOf(
                        SendMessage(
                            message.getSessionId(), "Правильно! ${Emojis.SUNGLASSES}" +
                                    "\n${previousTask.question} - ${previousTask.answers.joinToString("; ")}"
                        ),
                        nextTaskMessage
                    )
                } else {
                    args.remove(ArgKey.LESSON)
                    args.remove(ArgKey.SELECTED_SUBJECT)
                    transitionsHistoryConfigured.makeTransition(ProcSpace.DEFAULT)
                    listOf(SendMessage(
                        message.getSessionId(),
                        "Правильно! ${Emojis.SUNGLASSES}" +
                                "\n${previousTask.question} - ${previousTask.answers.joinToString("; ")}" +
                                "\nТы молодец! ${Emojis.CLAP}" +
                                "\nДо следующего занятия!"
                    ).replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD))
                }
            } else {
                listOf(
                    SendMessage(
                        message.getSessionId(),
                        "${Emojis.HMM}\nХмм, неправильно, попробуй снова"
                    )
                )
            }
        }
}