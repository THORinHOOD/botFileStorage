package com.thorinhood.botFarm.trainingBot.services

import com.thorinhood.botFarm.engine.ChatBot
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TimerService(
    private val sessionsService: SessionsService,
    private val chatBot: ChatBot,
    private val lessonService: LessonService
) {
    @Scheduled(fixedRate = 1000 * 60)
    fun tick() {
        val currentTimestamp = Date(System.currentTimeMillis())
        sessionsService.getAllSessions().forEach { session ->
            if (session.hasArg(ArgKey.SUBJECTS) &&
                (session.procSpace == ProcSpace.DEFAULT ||
                 session.procSpace == ProcSpace.SELECT_SUBJECT ||
                 session.procSpace == ProcSpace.IN_SUBJECT)) {
                @Suppress("UNCHECKED_CAST")
                val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
                subjects.forEach { (_, subject) ->
                    if (subject.timerConfig.checkAndUpdate(currentTimestamp)) {
                        if (session.procSpace == ProcSpace.DEFAULT ||
                            session.procSpace == ProcSpace.SELECT_SUBJECT ||
                            session.procSpace == ProcSpace.IN_SUBJECT) {
                            chatBot.sendMessages(lessonService.startLesson(session, subject))
                        }
                        sessionsService.updateSession(session)
                    }
                }
            }
        }
    }
}