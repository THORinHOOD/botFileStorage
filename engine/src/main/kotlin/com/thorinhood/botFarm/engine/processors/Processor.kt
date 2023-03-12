package com.thorinhood.botFarm.engine.processors

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.messages.HasProcSpace
import com.thorinhood.botFarm.engine.messages.HasSessionId
import java.util.function.Predicate

interface Processor<IR, IS>: HasProcSpace where IR: HasSessionId {
    var matcher: Predicate<IR>?
    fun process(message: IR, transitionsHistoryConfigured: TransitionsHistoryConfigured): List<IS>
}