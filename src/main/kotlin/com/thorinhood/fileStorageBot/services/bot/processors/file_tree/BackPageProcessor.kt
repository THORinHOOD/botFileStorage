package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.pagination.PaginationType
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor

@Processor
class BackPageProcessor(
    private val storagePageStrategy: StoragePageStrategy
) : BaseProcessor(
    "backPage",
    "file_tree"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult = ProcessResult(
        update.callbackQuery()?.let { callbackQuery ->
            storagePageStrategy.paginate(callbackQuery, session, PaginationType.BACK)
        } ?: listOf()
    )

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data().split("_")[0] == LABEL
        } ?: false

    companion object {
        const val LABEL = "back"
    }
}