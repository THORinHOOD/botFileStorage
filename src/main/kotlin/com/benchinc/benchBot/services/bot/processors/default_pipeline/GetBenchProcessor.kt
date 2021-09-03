package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.data.Point
import com.benchinc.benchBot.services.GeoService
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
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
            val benchId = text.substring(7)
            val bench = session.currentBenches.find { it.id == benchId }
            if (bench != null) {
                return listOf(buildMessageWithBench(session.chatId, bench.geometry))
            }
            return listOf()
//            val benchNode = geoService.findBenchById(benchId)
//            listOf(
//                if (bench == null) {
//                    incorrectBenchIdMessage(session.chatId)
//                } else {
//                    buildMessageWithBench(session.chatId, bench.geometry)
//                }
//            )
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    private fun incorrectBenchIdMessage(chatId: Long) : SendMessage =
        SendMessage(chatId, "Некорректный id лавочки")

    private fun buildMessageWithBench(chatId: Long, point: Point) : SendLocation =
        SendLocation(
            chatId,
            point.coordinates[1].toFloat(),
            point.coordinates[0].toFloat()
        )

    companion object {
        const val NAME = "bench"
    }
}