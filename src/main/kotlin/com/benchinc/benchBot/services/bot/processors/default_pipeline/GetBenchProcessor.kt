package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.db.benchLib.clients.BenchesServiceClient
import com.db.benchLib.data.PointGeo
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class GetBenchProcessor(
    private val benchesServiceClient: BenchesServiceClient,
    private val requestsCounter: Counter
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.text()?.let { text ->
            requestsCounter.labels("get_bench").inc()
            val benchId = text.substring(7)
            val bench = benchesServiceClient.findBenchById(benchId)
            if (bench != null) {
                return listOf(buildMessageWithBench(session.chatId, bench.geometry))
            }
            return listOf()
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    private fun buildMessageWithBench(chatId: Long, point: PointGeo) : SendLocation =
        SendLocation(
            chatId,
            point.coordinates[1].toFloat(),
            point.coordinates[0].toFloat()
        )

    companion object {
        const val NAME = "bench"
    }
}