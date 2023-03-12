package com.thorinhood.botFarm.engine.data.entities

class TransitionsHistoryConfigured(
    transitionsHistory: TransitionsHistory,
    private val capacity: Int,
    private val defaultProcSpace: String
) : TransitionsHistory(
    transitionsHistory.sessionId,
    transitionsHistory.history
) {
    fun makeTransition(procSpace: String) {
        makeTransition(procSpace, capacity)
    }

    fun getCurrentProcSpace() = getCurrentProcSpace(defaultProcSpace)
}