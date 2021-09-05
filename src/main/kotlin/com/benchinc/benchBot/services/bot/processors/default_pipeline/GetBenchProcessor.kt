package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchInfoStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.db.benchLib.clients.BenchesServiceClient
import com.db.benchLib.data.PointGeo
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import io.prometheus.client.Counter
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class GetBenchProcessor(
    private val benchesServiceClient: BenchesServiceClient,
    private val requestsCounter: Counter,
    private val benchInfoStrategy: BenchInfoStrategy
) : Processor, Logging {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.text()?.let { text ->
            requestsCounter.labels("get_bench").inc()
            val benchId = text.substring(7)
            val bench = benchesServiceClient.findBenchById(benchId)
            val messages: MutableList<BaseRequest<*, *>> = mutableListOf()
            if (bench != null) {
                try {
                    val photo = benchesServiceClient.getBenchPhoto(benchId)
                    if (photo != null) {
                        messages.add(SendPhoto(session.chatId, photo)
                            .caption(benchInfoStrategy.description(bench)))
                    } else {
                        messages.add(SendMessage(session.chatId, "Нет фотографии\n" +
                                benchInfoStrategy.description(bench)))
                    }
                } catch (e:Exception) {
                    logger.error(e)
                    messages.add(SendMessage(session.chatId, "Нет фотографии\n" +
                            benchInfoStrategy.description(bench)))
                }
                messages.add(buildMessageWithBench(session.chatId, bench.geometry))
            }
            return messages
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