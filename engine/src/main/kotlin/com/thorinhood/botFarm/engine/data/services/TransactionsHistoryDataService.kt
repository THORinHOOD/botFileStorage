package com.thorinhood.botFarm.engine.data.services

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistory
import com.thorinhood.botFarm.engine.data.services.base.DataService

class TransactionsHistoryDataService(
    private val dataService: DataService,
    private val defaultProcSpace: String,
    private val historyCapacity: Int
) : DataService by dataService {

    fun <R> workWith(sessionId: Long, process: (TransitionsHistory) -> R) =
        maintainWrap(sessionId, TransitionsHistory::class, { getDefault(sessionId) }, process)

    fun getBySessionId(sessionId: Long): TransitionsHistory =
        dataService.getOneBySessionId(sessionId, TransitionsHistory::class) {
            getDefault(sessionId)
        }

    private fun getDefault(sessionId: Long) =
        TransitionsHistory(sessionId, defaultProcSpace, historyCapacity)

}