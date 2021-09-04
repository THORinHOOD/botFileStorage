package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.db.benchLib.clients.BenchesServiceClient
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class FindBenchesProcessor(private val benchesServiceClient: BenchesServiceClient,
                           private val benchPageStrategy: BenchPageStrategy,
                           private val requestsCounter: Counter
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.location()?.let { location ->
            requestsCounter.labels("find_benches").inc()
            val response = benchesServiceClient.findBenchesNear(
                location.longitude().toDouble(),
                location.latitude().toDouble(),
                session.radius.toDouble()/1000,
                0, 5)
            if (response.benches.isEmpty()) {
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