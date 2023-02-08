package com.thorinhood.botFarm.engine.processors.data

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistory

data class Session(
    val sessionId: Long,
    val transitionsHistory: TransitionsHistory
)