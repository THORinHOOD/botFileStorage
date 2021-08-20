package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.GeoService
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import de.westnordost.osmapi.map.data.Node
import org.springframework.stereotype.Service

@Service
class GetBenchProcessor(val geoService: GeoService) : CommandProcessor {

    override fun getCommandName(): String = "bench"

    override fun process(session: Session, text: String): AbstractSendRequest<*> {
        val benchId = text.substring(7).toLong()

        val bench = session.currentBenches.find { it.node.id == benchId }
        if (bench != null) {
            return buildMessageWithBench(session.chatId, bench.node)
        }

        val benchNode = geoService.findBenchById(benchId)
        return if (benchNode == null) {
            SendMessage(session.chatId, "Некорректный id лавочки")
        } else {
            buildMessageWithBench(session.chatId, benchNode)
        }
    }

    private fun buildMessageWithBench(chatId: Long, benchNode: Node) : SendLocation =
        SendLocation(
            chatId,
            benchNode.position.latitude.toFloat(),
            benchNode.position.longitude.toFloat()
        )

}