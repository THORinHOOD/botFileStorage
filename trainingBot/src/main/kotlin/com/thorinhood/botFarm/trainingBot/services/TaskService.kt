package com.thorinhood.botFarm.trainingBot.services

import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.sessions.Session
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val googleTableService: GoogleTableService,
) {

    fun buildMessageWithTask(session: Session<Long>) : BaseRequest<*, *> {
        val task = googleTableService.getTask(session).get()
        session.cursor.args["answers"] = task.answers
        return SendMessage(
            session.sessionId,
            "Переведи:\n${task.question}"
        )
    }

}