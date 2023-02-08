package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val lessonService: LessonService,
    private val schedulingManager: SchedulingManager,
    private val argumentsDataService: SessionArgumentsDataService,
    private val transitionsHistoryDataService: TransactionsHistoryDataService
) : Logging {

    @Async
    fun rescheduleSubject(sessionId: Long, subject: Subject) {
        schedulingManager.removeTask(subject.scheduleConfig.taskId)
        scheduleSubject(sessionId, subject)
    }

    @Async
    fun scheduleSubject(sessionId: Long, subject: Subject) {
        schedulingManager.addTask(
            subject.scheduleConfig,
            sessionId,
            mapOf("subject_name" to subject.name),
            { innerSessionId, arguments ->
                argumentsDataService.maintainWrap(innerSessionId) { sessionArguments ->
                    val allSubjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
                    transitionsHistoryDataService.workWith(innerSessionId) { transitionsHistory ->
                        if (transitionsHistory.getCurrentProcSpace() == ProcSpace.LESSON) {
                            listOf(
                                SendMessage(
                                    innerSessionId,
                                    "${Emojis.HAND_WAVE}\nКажется ты пропустил занятие!"
                                )
                            )
                        } else {
                            allSubjects[arguments["subject_name"]]?.let {
                                lessonService.startLesson(
                                    Session(innerSessionId, transitionsHistory),
                                    sessionArguments,
                                    it
                                )
                            } ?: listOf() // TODO scheduled subject that doesn't exist anymore
                        }
                    }
                }
            },
            { innerSessionId ->
                val sessionArguments = argumentsDataService.getBySessionId(innerSessionId)
                val innerSubjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
                innerSubjects[subject.name]?.scheduleConfig?.period ?: -1L
            })
    }

}