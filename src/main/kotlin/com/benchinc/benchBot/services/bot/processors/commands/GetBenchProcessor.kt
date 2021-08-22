package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.GeoService
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import de.westnordost.osmapi.map.data.Node
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
class GetBenchProcessor(val geoService: GeoService,
                        private val requestsCounter: Counter) : CommandProcessor {

    override fun getCommandName(): String = "bench"

    override fun process(session: Session, parameter: String): List<BaseRequest<*, *>> {
        requestsCounter.labels("get_bench").inc()
        val benchId = parameter.substring(7).toLong()
        val bench = session.currentBenches.find { it.node.id == benchId }
        if (bench != null) {
            return listOf(buildMessageWithBench(session.chatId, bench.node))
        }
        val benchNode = geoService.findBenchById(benchId)
        return listOf( if (benchNode == null) {
            SendMessage(session.chatId, "Некорректный id лавочки")
        } else {
            buildMessageWithBench(session.chatId, benchNode)
        })
    }

    private fun buildMessageWithBench(chatId: Long, benchNode: Node) : SendLocation =
        SendLocation(
            chatId,
            benchNode.position.latitude.toFloat(),
            benchNode.position.longitude.toFloat()
        )

}