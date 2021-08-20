package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.GeoService
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class GetBenchProcessor(val geoService: GeoService) : CommandProcessor {

    override fun getCommandName(): String = "bench"

    override fun process(session: Session, text: String): AbstractSendRequest<*> {
        val benchId = text.substring(7).toLong()
        val bench = geoService.findBenchById(benchId)
        return if (bench == null) {
            SendMessage(session.chatId, "Некорректный id лавочки")
        } else {
            SendLocation(
                session.chatId,
                bench.position.latitude.toFloat(),
                bench.position.longitude.toFloat()
            )
        }
    }

}