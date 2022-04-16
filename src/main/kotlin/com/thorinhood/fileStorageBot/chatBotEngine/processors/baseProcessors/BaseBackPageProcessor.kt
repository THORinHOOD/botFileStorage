package com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationType
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.BasePageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

abstract class BaseBackPageProcessor<T>(
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
            basePageStrategy.paginate(callbackQuery, session, PaginationType.BACK)
        } ?: listOf()
    )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data().split("_")[0] == LABEL
        } ?: false

    companion object {
        const val LABEL = "back"
    }
}