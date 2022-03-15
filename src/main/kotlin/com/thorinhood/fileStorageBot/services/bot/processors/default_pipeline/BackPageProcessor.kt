package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline

import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.pagination.PaginationType
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class BackPageProcessor(
    private val storagePageStrategy: StoragePageStrategy
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.callbackQuery()?.let { callbackQuery ->
            storagePageStrategy.paginate(callbackQuery, session, PaginationType.BACK)
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data().split("_")[0] == NAME
        } ?: false

    companion object {
        const val NAME = "back"
    }
}