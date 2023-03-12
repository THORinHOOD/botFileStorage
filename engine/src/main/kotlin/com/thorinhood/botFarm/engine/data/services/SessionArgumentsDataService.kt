package com.thorinhood.botFarm.engine.data.services

import com.thorinhood.botFarm.engine.data.entities.SessionArguments
import com.thorinhood.botFarm.engine.data.services.base.DataService

class SessionArgumentsDataService(
    private val dataService: DataService
) : DataService by dataService {

    fun <R : Any> maintainWrap(sessionId: Long, process: (SessionArguments) -> R) =
        maintainWrap(sessionId, SessionArguments::class, { getDefault(sessionId) }, process)

    fun getBySessionId(sessionId: Long) =
        dataService.getOneBySessionId(sessionId, SessionArguments::class) {
            getDefault(sessionId)
        }

    fun getAll() = dataService.getAll(SessionArguments::class)

    private fun getDefault(sessionId: Long) = SessionArguments(sessionId, mutableMapOf())
}