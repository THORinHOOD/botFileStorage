package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val sessionsService: SessionsService,
    private val lessonService: LessonService,
    private val schedulingManager: SchedulingManager
) {

    @Async
    fun rescheduleSubject(session: Session<Long>, subject: Subject) {
        schedulingManager.removeTask(subject.scheduleConfig.taskId)
        scheduleSubject(session, subject)
    }

    @Async
    fun scheduleSubject(session: Session<Long>, subject: Subject) {
        val subjectName = subject.name
        schedulingManager.addTask(
            subject.scheduleConfig.taskId,
            session.sessionId,
            { it.sendMessages(lessonService.startLesson(session, subject)) },
            { sessionId ->
                val innerSession = sessionsService.getSession(sessionId)
                val innerSubjects = innerSession.get<AllSubjects>(ArgKey.SUBJECTS)
                if (!innerSubjects.containsKey(subjectName)) {
                    -1L
                } else {
                    innerSubjects[subjectName]!!.scheduleConfig.period
                }
            })
    }

}