package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.benchLib.proto.common.Location
import com.thorinhood.benchLib.proto.common.PageInfo
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import com.thorinhood.benchLib.proto.services.FindBenchesNearRequest
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class FindBenchesProcessor(
    private val benchServiceStub: BenchServiceGrpc.BenchServiceBlockingStub,
    private val benchPageStrategy: BenchPageStrategy,
    private val requestsCounter: Counter
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.location()?.let { location ->
            requestsCounter.labels("find_benches").inc()

            val response = benchServiceStub.findBenchesNear(
                FindBenchesNearRequest.newBuilder()
                    .setLocation(
                        Location.newBuilder()
                            .setLon(location.longitude().toDouble())
                            .setLat(location.latitude().toDouble())
                            .build()
                    )
                    .setPageInfo(
                        PageInfo.newBuilder()
                            .setIndex(0)
                            .setSize(5)
                            .build()
                    )
                    .setRadius(session.radius.toDouble() / 1000)
                    .build()
            )

            if (response.paginationInfo.elementsCount == 0) {
                listOf(
                    SendMessage(
                        session.chatId, "В радиусе ${session.radius} метров " +
                                "лавочек не найдено \uD83D\uDE1E"
                    )
                )
            } else {
                benchPageStrategy.buildPageWithBenches(response, session.chatId, null)
            }
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.location() != null

    companion object {
        const val NAME = "\uD83D\uDCCD Поиск лавочек"
    }

}