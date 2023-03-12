package com.thorinhood.botFarm.engine.data.services

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistory
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.base.DataService

class TransactionsHistoryDataService(
    private val dataService: DataService,
    private val defaultProcSpace: String,
    private val historyCapacity: Int
) : DataService by dataService {

    fun <R> workWith(sessionId: Long, process: (TransitionsHistoryConfigured) -> R) =
        maintainWrap(sessionId, TransitionsHistory::class, { TransitionsHistory(sessionId) }, { transitionsHistory ->
            process(TransitionsHistoryConfigured(transitionsHistory, historyCapacity, defaultProcSpace))
        })

    fun getBySessionId(sessionId: Long): TransitionsHistoryConfigured =
        TransitionsHistoryConfigured(dataService.getOneBySessionId(sessionId, TransitionsHistory::class) {
            getDefault(sessionId)
        }, historyCapacity, defaultProcSpace)

    private fun getDefault(sessionId: Long) =
        TransitionsHistoryConfigured(TransitionsHistory(sessionId), historyCapacity, defaultProcSpace)

}