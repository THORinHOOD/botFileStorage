package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.scheduling.ScheduleConfig
import com.thorinhood.botFarm.telegram.TelegramReceiveMessageWrapper
import com.thorinhood.botFarm.telegram.TelegramSendMessage
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import java.util.function.Predicate

class InputSubjectGoogleTableSheetProcessor(
    private val subjectService: SubjectService,
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : Processor<TelegramReceiveMessageWrapper, TelegramSendMessage> {

    override var matcher: Predicate<TelegramReceiveMessageWrapper>? = null
    override var procSpace: String = ""

    override fun process(
        message: TelegramReceiveMessageWrapper,
        transitionsHistoryConfigured: TransitionsHistoryConfigured
    ): List<TelegramSendMessage> {
        val googleTableSheet = message.value.message()?.text() ?: throw Exception("Попробуй ещё раз")
        val subject = sessionArgumentsDataService.maintainWrap(message.getSessionId()) { args ->
            val builder = args.get<Subject.Builder>(ArgKey.SUBJECT_BUILDER).googleTableSheet(googleTableSheet)
            val allSubjects = args.getOrPut(ArgKey.SUBJECTS) { mutableMapOf<String, Subject>() }
            val subject = builder.scheduleConfig(
                ScheduleConfig(
                    "subject_${builder.name}_${message.getSessionId()}",
                    1 * 60 * 60 * 1000
                )
            ).build()
            allSubjects[subject.name] = subject
            args.remove(ArgKey.SUBJECT_BUILDER)
            subject
        }
        transitionsHistoryConfigured.makeTransition(ProcSpace.DEFAULT)
        subjectService.scheduleSubject(message.getSessionId(), subject)
        return listOf(
            SendMessage(
                message.getSessionId(),
                "Готово!\n" +
                        "Добавлен новый предмет!"
            ).replyMarkup(KeyboardMarkups.DEFAULT_KEYBOARD)
        )
    }
}