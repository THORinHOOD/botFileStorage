package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.ChatBot
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TimerService(
    private val sessionsService: SessionsService,
    private val chatBot: ChatBot,
    private val lessonService: LessonService
) {
    @Scheduled(fixedRate = 1000 * 60)
    fun tick() {
        sessionsService.getAllSessions().forEach { session ->
            if (session.hasArg(ArgKey.TIMER_CONFIG) &&
                session.cursor.procSpace == ProcSpace.DEFAULT) {
                val timer = session.args[ArgKey.TIMER_CONFIG] as TimerConfig
                timer.counter += 1
                if (timer.counter == timer.interval) {
                    timer.counter = 0
                    if (session.cursor.procSpace == ProcSpace.DEFAULT) {
                        chatBot.sendMessages(lessonService.startLesson(session))
                    }
                }
                sessionsService.updateSession(session)
            }
        }
    }
}