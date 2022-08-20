package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.ChatBot
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TaskGiverService(
    private val sessionsService: SessionsService,
    private val chatBot: ChatBot,
    private val taskService: TaskService
) {

    @Scheduled(fixedRate = 1000 * 60)
    fun sendTask() {
        sessionsService.getAllSessions().forEach { session ->
            if (session.args.containsKey(ArgKey.TIMER_CONFIG) &&
                session.cursor.procSpace == ProcSpace.DEFAULT) {
                val timer = session.args[ArgKey.TIMER_CONFIG] as TimerConfig
                timer.counter += 1
                if (timer.counter == timer.interval) {
                    timer.counter = 0
                    if (session.cursor.procSpace == ProcSpace.DEFAULT) {
                        session.cursor.procSpace = ProcSpace.LESSON
                        session.args[ArgKey.LESSON_TASKS_REMAIN] = (session.args[ArgKey.TIMER_CONFIG] as TimerConfig).size - 1
                        chatBot.sendMessages(listOf(
                            SendMessage(
                                session.sessionId,
                                "Пора начинать занятие!"
                            ),
                            taskService.buildMessageWithTask(session)
                        ))
                        sessionsService.updateSession(session)
                    }
                }
            }
        }
    }

}