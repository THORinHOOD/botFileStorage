package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.ChatBot
import com.thorinhood.botFarm.engine.sessions.SessionsService
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
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
            if (session.args.containsKey("timer_config") &&
                session.cursor.procSpace == "default") {
                val timer = session.args["timer_config"] as TimerConfig
                timer.counter += 1
                if (timer.counter == timer.interval) {
                    timer.counter = 0
                    if (session.cursor.procSpace == "default") {
                        session.cursor.procSpace = "lesson"
                        session.args["lesson_tasks_remain"] = (session.args["timer_config"] as TimerConfig).size - 1
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