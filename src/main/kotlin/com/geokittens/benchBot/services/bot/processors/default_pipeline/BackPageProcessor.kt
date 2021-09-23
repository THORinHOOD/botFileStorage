package com.geokittens.benchBot.services.bot.processors.default_pipeline

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.geokittens.benchBot.services.bot.helpers.strategies.PaginationType
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import com.geokittens.benchBot.services.bot.util.PageCallback
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import com.thorinhood.benchLib.proto.services.FindBenchesNearRequest
import org.springframework.stereotype.Service
import com.thorinhood.benchLib.proto.common.Location
import com.thorinhood.benchLib.proto.common.PageInfo

@Service
@Pipeline("default")
class BackPageProcessor(
    private val benchPageStrategy: BenchPageStrategy
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.callbackQuery()?.let { callbackQuery ->
            benchPageStrategy.paginate(callbackQuery, session.chatId, PaginationType.BACK)
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data().split("_")[0] == NAME
        } ?: false

    companion object {
        const val NAME = "back"
    }
}