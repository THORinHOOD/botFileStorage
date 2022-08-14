package com.thorinhood.botFarm.engine.processors.baseProcessors

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.pagination.BasePageStrategy
import com.thorinhood.botFarm.engine.pagination.PaginationType
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.sessions.Session

abstract class BaseForwardPageProcessor<T>(
    private val basePageStrategy: BasePageStrategy<T>,
    name: String,
    procSpace: String
) : BaseProcessor(
    name,
    procSpace
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult = ProcessResult(
        update.callbackQuery()?.let { callbackQuery ->
            basePageStrategy.paginate(callbackQuery, session, PaginationType.FORWARD)
        } ?: listOf()
    )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data()
                .split("_")[0] == LABEL
        } ?: false

    companion object {
        const val LABEL = "forward"
    }
}