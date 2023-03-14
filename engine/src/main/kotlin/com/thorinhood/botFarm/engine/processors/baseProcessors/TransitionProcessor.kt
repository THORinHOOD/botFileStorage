package com.thorinhood.botFarm.engine.processors.baseProcessors

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.messages.HasSessionId
import com.thorinhood.botFarm.engine.processors.Processor
import java.util.function.Predicate

open class TransitionProcessor<IR: HasSessionId, IS>(
    private val to: String,
    private val transitionMessageBuilder: (IR) -> IS,
    private val action: ((IR) -> Unit)? = null
): Processor<IR, IS> {

    override var matcher: Predicate<IR>? = null
    override var defaultTransition: String? = null
    override var procSpace: String = ""

    override fun process(message: IR, transitionsHistoryConfigured: TransitionsHistoryConfigured): List<IS> {
        transitionsHistoryConfigured.makeTransition(to)
        action?.invoke(message)
        return listOf(transitionMessageBuilder.invoke(message))
    }
}