package com.thorinhood.botFarm.trainingBot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.Space
import com.thorinhood.botFarm.engine.bot
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import com.thorinhood.botFarm.engine.processors.baseProcessors.TransitionProcessor
import com.thorinhood.botFarm.engine.processors.matchers.Matchers.Companion.eq
import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import com.thorinhood.botFarm.telegram.*
import com.thorinhood.botFarm.trainingBot.domain.TelegramReceiverConverter
import com.thorinhood.botFarm.trainingBot.domain.TelegramSenderConverter
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.spaces.WelcomeProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.add.InputSubjectGoogleTableIdProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.add.InputSubjectGoogleTableSheetProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.add.InputSubjectNameProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.lesson.IDKProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.lesson.LessonProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.lesson.StartLessonProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.select.SelectSubjectProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.select.StartSelectSubjectProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period.ChangePeriodProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period.StartChangePeriodProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.settings.size.ChangeSizeProcessor
import com.thorinhood.botFarm.trainingBot.spaces.subject.settings.size.StartChangeSizeProcessor
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.ScheduledExecutorService

@Configuration
class BotConfig : DisposableBean {

    @Autowired
    lateinit var scheduledTaskRegistrar: ScheduledTaskRegistrar

    @Bean
    fun telegramBotSender(telegramBot: TelegramBot) =
        TelegramBotSender(telegramBot, TelegramSenderConverter())

    @Bean
    fun schedulingManager(
        telegramBotSender: TelegramBotSender<TelegramSendMessage>
    ): SchedulingManager<TelegramSendMessage, TelegramSendMessage> =
        SchedulingManager(scheduledTaskRegistrar, telegramBotSender)

    @Bean
    fun trainingBot(
        telegramBot: TelegramBot,
        telegramBotSender: TelegramBotSender<TelegramSendMessage>,
        transitionsDataService: TransactionsHistoryDataService,
        sessionArgumentsDataService: SessionArgumentsDataService,
        scheduledExecutorService: ScheduledExecutorService,
        subjectService: SubjectService,
        lessonService: LessonService,
        schedulingManager: SchedulingManager<TelegramSendMessage, TelegramSendMessage>
    ) =
        bot<TelegramReceiveMessage, TelegramReceiveMessageWrapper, TelegramSendMessage, TelegramSendMessage> {
            receiver(TelegramBotReceiver(telegramBot, TelegramReceiverConverter()))
            sender(telegramBotSender)
            schedulingManager(schedulingManager)
            smthWentWrongMessageBuilder {
                SendMessage(it.getSessionId(), "Что-то пошло не так")
            }
            notFoundAnyProcessor {
                SendMessage(it.getSessionId(), "Что-то я тебя не понимаю, попробуй снова")
            }
            space(ProcSpace.DEFAULT) {
                processor(WelcomeProcessor()) {
                    matcher = eq("/start")
                }
                processor(StartSelectSubjectProcessor(sessionArgumentsDataService)) {
                    matcher = eq("Перейти к предмету")
                }
                transition(
                    ProcSpace.INPUT_SUBJECT_NAME,
                    "Введи имя нового предмета",
                    KeyboardMarkups.CANCEL_KEYBOARD,
                    "Добавить предмет"
                )
            }
            space(ProcSpace.INPUT_SUBJECT_NAME) {
                defaultProcessor(InputSubjectNameProcessor(sessionArgumentsDataService))
                transitionToDefault("Окей, не будем", "Отмена")
            }
            space(ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_ID) {
                defaultProcessor(InputSubjectGoogleTableIdProcessor(sessionArgumentsDataService))
                transitionToDefault("Окей, не будем", "Отмена")
            }
            space(ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_SHEET) {
                defaultProcessor(InputSubjectGoogleTableSheetProcessor(subjectService, sessionArgumentsDataService))
                transitionToDefault("Окей, не будем", "Отмена")
            }
            space(ProcSpace.SELECT_SUBJECT) {
                defaultProcessor(SelectSubjectProcessor(sessionArgumentsDataService))
                transitionToDefault("Окей, не будем", "Назад")
            }
            space(ProcSpace.IN_SUBJECT) {
                processor(StartChangePeriodProcessor(sessionArgumentsDataService)) {
                    matcher = eq("Изменить интервал выдачи заданий")
                }
                processor(StartChangeSizeProcessor(sessionArgumentsDataService)) {
                    matcher = eq("Изменить кол-во заданий в выдаче")
                }
                processor(StartLessonProcessor(lessonService, sessionArgumentsDataService)) {
                    matcher = eq("Начать занятие")
                }
                transitionToDefault("Окей, что дальше?", "Назад") {
                    sessionArgumentsDataService.maintainWrap(it.getSessionId()) { args ->
                        args.remove(ArgKey.SELECTED_SUBJECT)
                    }
                }
            }
            space(ProcSpace.LESSON) {
                defaultProcessor(LessonProcessor(lessonService, sessionArgumentsDataService))
                processor(IDKProcessor(sessionArgumentsDataService)) {
                    matcher = eq("Не знаю")
                }
                transitionToDefault("Okaaaay, let's stop!", "Закончить занятие") {
                    sessionArgumentsDataService.maintainWrap(it.getSessionId()) { args ->
                        args.remove(ArgKey.LESSON)
                        args.remove(ArgKey.SELECTED_SUBJECT)
                    }
                }
            }
            space(ProcSpace.CHANGE_PERIOD) {
                defaultProcessor(ChangePeriodProcessor(subjectService, sessionArgumentsDataService))
                transition(ProcSpace.IN_SUBJECT, "Окей, не будем", KeyboardMarkups.SUBJECT_KEYBOARD, "Отмена")
            }
            space(ProcSpace.CHANGE_SIZE) {
                defaultProcessor(ChangeSizeProcessor(sessionArgumentsDataService))
                transition(ProcSpace.IN_SUBJECT, "Окей, не будем", KeyboardMarkups.SUBJECT_KEYBOARD, "Отмена")
            }
        }.start(transitionsDataService)

    override fun destroy() {
        scheduledTaskRegistrar.destroy()
    }
}

fun Space<TelegramReceiveMessageWrapper, TelegramSendMessage>.transitionToDefault(
    transitionMessage: String,
    caption: String,
    action: ((TelegramReceiveMessageWrapper) -> Unit)? = null
) {
    transition(ProcSpace.DEFAULT, transitionMessage, KeyboardMarkups.DEFAULT_KEYBOARD, caption, action)
}

fun Space<TelegramReceiveMessageWrapper, TelegramSendMessage>.transition(
    to: String,
    transitionMessage: String,
    keyboard: ReplyKeyboardMarkup,
    caption: String,
    action: ((TelegramReceiveMessageWrapper) -> Unit)? = null
) {
    processor(TransitionProcessor(to, { message ->
        SendMessage(
            message.getSessionId(),
            transitionMessage
        ).replyMarkup(keyboard)
    }, action)) {
        matcher = eq(caption)
    }
}