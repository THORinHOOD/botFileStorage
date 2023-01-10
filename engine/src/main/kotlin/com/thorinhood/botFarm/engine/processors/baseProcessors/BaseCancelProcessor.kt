package com.thorinhood.botFarm.engine.processors.baseProcessors

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.BaseTransition
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.sessions.Session

abstract class BaseCancelProcessor(
    name: String,
    procSpace: String = "",
    private val transition: BaseTransition
) : BaseProcessor(name, procSpace) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        return ProcessResult(null, transition)
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Отмена"
    }

}