package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.benchLib.proto.common.Location
import com.thorinhood.benchLib.proto.common.PageInfo
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import com.thorinhood.benchLib.proto.services.FindBenchesNearRequest
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class ForwardPageProcessor(
    private val benchServiceStub: BenchServiceGrpc.BenchServiceBlockingStub,
    private val benchPageStrategy: BenchPageStrategy
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.callbackQuery()?.let { callbackQuery ->
            benchPageStrategy.withCallbackData(callbackQuery, session.chatId, this::processWithData)
        } ?: listOf()

    private fun processWithData(
        chatId: Long, callbackQueryId: String, page: Int, pageSize: Int, lat: Double,
        lon: Double, radius: Double
    ): List<BaseRequest<*, *>> {
        val response = benchServiceStub.findBenchesNear(
            FindBenchesNearRequest.newBuilder()
                .setLocation(
                    Location.newBuilder()
                        .setLon(lon)
                        .setLat(lat)
                        .build()
                )
                .setPageInfo(
                    PageInfo.newBuilder()
                        .setIndex(page + 1)
                        .setSize(pageSize)
                        .build()
                )
                .setRadius(radius)
                .build()
        )

        return if (response.paginationInfo.elementsCount == 0) {
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