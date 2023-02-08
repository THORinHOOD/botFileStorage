package com.thorinhood.botFarm.trainingBot.initilzr

import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class InitilzrService(
    private val argumentsDataService: SessionArgumentsDataService,
    private val subjectService: SubjectService
) : InitializingBean {
    override fun afterPropertiesSet() {
        argumentsDataService.getAll().forEach { sessionArguments ->
            if (sessionArguments.containsKey(ArgKey.SUBJECTS)) {
                sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS).forEach { (_, subject) ->
                    subjectService.scheduleSubject(sessionArguments.sessionId, subject)
                }
            }
        }
    }
}