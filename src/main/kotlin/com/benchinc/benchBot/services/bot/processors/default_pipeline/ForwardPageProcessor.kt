package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.db.benchLib.clients.BenchesServiceClient
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class ForwardPageProcessor(private val benchesServiceClient: BenchesServiceClient,
                           private val benchPageStrategy: BenchPageStrategy) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.callbackQuery()?.let { callbackQuery ->
            benchPageStrategy.withCallbackData(callbackQuery, session.chatId, this::processWithData)
        } ?: listOf()

    private fun processWithData(chatId: Long, callbackQueryId: String, page: Int, pageSize: Int, lat: Double,
                                lon: Double, radius: Double): List<BaseRequest<*, *>> {
        val response = benchesServiceClient.findBenchesNear(lon, lat, radius, page + 1, pageSize)
        return if (response.benches.isEmpty()) {
            listOf(AnswerCallbackQuery(callbackQueryId))
        } else {
            benchPageStrategy.buildPageWithBenches(response, chatId, callbackQueryId)
        }
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.data().split("_")[0] == NAME
        } ?: false

    companion object {
        const val NAME = "forward"
    }
}