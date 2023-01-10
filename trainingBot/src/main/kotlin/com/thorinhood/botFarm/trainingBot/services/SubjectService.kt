package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.configs.TelegramMessage
import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import com.thorinhood.botFarm.engine.sessions.Session
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
    private val schedulingManager: SchedulingManager
) : Logging {

    @Async
    fun rescheduleSubject(session: Session<Long>, subject: Subject) {
        schedulingManager.removeTask(subject.scheduleConfig.taskId)
        scheduleSubject(session, subject)
    }

    @Async
    fun scheduleSubject(session: Session<Long>, subject: Subject) {
        schedulingManager.addTask(
            subject.scheduleConfig,
            session.sessionId,
            mapOf("subject_name" to subject.name),
            { innerSession, arguments ->
                val allSubjects = innerSession.get<AllSubjects>(ArgKey.SUBJECTS)
                if (innerSession.transitionsHistory.currentProcSpace() == ProcSpace.LESSON) {
                    listOf(
                        SendMessage(
                            innerSession.sessionId,
                        "${Emojis.HAND_WAVE}\nКажется ты пропустил занятие!"
                        )
                    )
                } else {
                    allSubjects[arguments["subject_name"]]?.let { innerSubject ->
                        lessonService.startLesson(innerSession, innerSubject)
                    } ?: listOf() // TODO scheduled subject that doesn't exist anymore
                }
            },
            { innerSession ->
                val innerSubjects = innerSession.get<AllSubjects>(ArgKey.SUBJECTS)
                innerSubjects[subject.name]?.scheduleConfig?.period ?: -1L
            })
    }

}