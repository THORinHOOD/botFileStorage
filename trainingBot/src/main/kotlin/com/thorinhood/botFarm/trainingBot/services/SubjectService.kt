package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
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
                if (innerSession.procSpace == ProcSpace.DEFAULT) {
                    val allSubjects = innerSession.get<AllSubjects>(ArgKey.SUBJECTS)
                    allSubjects[arguments["subject_name"]]?.let { innerSubject ->
                        lessonService.startLesson(innerSession, innerSubject)
                    } ?: listOf() // TODO what if nothing to send
                } else {
                    listOf()
                }
            },
            { innerSession ->
                val innerSubjects = innerSession.get<AllSubjects>(ArgKey.SUBJECTS)
                innerSubjects[subject.name]?.scheduleConfig?.period ?: -1L
            })
    }

}