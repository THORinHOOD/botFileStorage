package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.GeoService
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import de.westnordost.osmapi.map.data.Node
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class GetBenchProcessor(val geoService: GeoService,
                        private val requestsCounter: Counter) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.text()?.let { text ->
            requestsCounter.labels("get_bench").inc()
            val benchId = text.substring(7).toLongOrNull() ?:
                return listOf(incorrectBenchIdMessage(session.chatId))
            val bench = session.currentBenches.find { it.node.id == benchId }
            if (bench != null) {
                return listOf(buildMessageWithBench(session.chatId, bench.node))
            }
            val benchNode = geoService.findBenchById(benchId)
            listOf(
                if (benchNode == null) {
                    incorrectBenchIdMessage(session.chatId)
                } else {
                    buildMessageWithBench(session.chatId, benchNode)
                }
            )
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    private fun incorrectBenchIdMessage(chatId: Long) : SendMessage =
        SendMessage(chatId, "Некорректный id лавочки")

    private fun buildMessageWithBench(chatId: Long, benchNode: Node) : SendLocation =
        SendLocation(
            chatId,
            benchNode.position.latitude.toFloat(),
            benchNode.position.longitude.toFloat()
        )

    companion object {
        const val NAME = "bench"
    }
}