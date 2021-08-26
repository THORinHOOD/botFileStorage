package com.benchinc.benchBot.services.bot.processors.managers

import com.benchinc.benchBot.data.AllSessions
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class MainProcessorsManagerImpl(private val processorsManagers: List<ProcessorsManager>) : MainProcessorsManager {

    override fun process(allSessions: AllSessions, update: Update) : List<BaseRequest<*, *>> {
        return processorsManagers.mapNotNull { processorsManager -> processorsManager.process(allSessions, update) }
            .let { responses ->
                if (responses.isEmpty()) listOf() else responses.flatMap { it.toList() }
            }
    }

}