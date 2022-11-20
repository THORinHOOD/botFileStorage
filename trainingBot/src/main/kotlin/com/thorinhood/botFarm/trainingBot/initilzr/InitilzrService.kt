package com.thorinhood.botFarm.trainingBot.initilzr

import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class InitilzrService(
    private val sessionsService: SessionsService,
    private val subjectService: SubjectService
) : InitializingBean {
    override fun afterPropertiesSet() {
        sessionsService.getAllSessions().forEach { session ->
            if (session.containsKey(ArgKey.SUBJECTS)) {
                session.get<AllSubjects>(ArgKey.SUBJECTS).forEach { (_, subject) ->
                    subjectService.scheduleSubject(session, subject)
                }
            }
        }
    }
}