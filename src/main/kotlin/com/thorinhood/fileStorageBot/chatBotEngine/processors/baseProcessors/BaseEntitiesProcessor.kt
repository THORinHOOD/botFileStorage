package com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors

import com.thorinhood.fileStorageBot.bot.FileStorageService
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.BasePageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor

import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

abstract class BaseEntitiesProcessor<T>(
    private val fileStorageService: FileStorageService<T>,
    private val storagePageStrategy: BasePageStrategy<T>,
    private val tokenKey: String,
    name: String,
    procSpace: String = "",
) : BaseProcessor(
    name,
    procSpace
) {

    protected fun getEntities(session: Session<Long>) : ProcessResult {
        val paginationContext = getContext(session)
        val currentPath = paginationContext.context["currentPath"] as String
        val response = fileStorageService.getEntities(
            (session.args[tokenKey] as String),
            currentPath,
            paginationContext.offset,
            paginationContext.limit)
        return ProcessResult(
            if (response.entities.isEmpty()) {
                null
            } else {
                storagePageStrategy.buildPage(response, session, null)
            },
            buildTransition(response.entities.isEmpty(), currentPath))
    }

    abstract fun getContext(session: Session<Long>) : PaginationContext<T>
    protected abstract fun buildTransition(hasEntities: Boolean, extraArg: String) : Transition
}