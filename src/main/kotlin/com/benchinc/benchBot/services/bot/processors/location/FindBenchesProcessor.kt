package com.benchinc.benchBot.services.bot.processors.location

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.GeoService
import com.benchinc.benchBot.services.bot.processors.helpers.BenchPageStrategy
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
class FindBenchesProcessor(private val geoService: GeoService,
                           private val benchPageStrategy: BenchPageStrategy,
                           private val requestsCounter: Counter
) : LocationProcessor {

    override fun process(session: Session, parameter: Location): List<BaseRequest<*, *>> {
        requestsCounter.labels("find_benches").inc()
        session.currentBenches = geoService.findBenches(parameter, session.radius)
        return if (session.currentBenches.isEmpty()) {
            listOf(SendMessage(session.chatId, "В радиусе ${session.radius} метров " +
                    "лавочек не найдено \uD83D\uDE1E"))
        } else {
            benchPageStrategy.buildPageWithBenches(session, 0, null)
        }
    }

}