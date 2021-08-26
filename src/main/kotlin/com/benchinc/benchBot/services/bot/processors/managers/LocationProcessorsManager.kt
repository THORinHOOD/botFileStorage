package com.benchinc.benchBot.services.bot.processors.managers

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.PipelineStrategy
import com.benchinc.benchBot.services.bot.processors.helpers.strategies.ProcessStrategy
import com.benchinc.benchBot.services.bot.processors.location.LocationProcessor
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class LocationProcessorsManager(private val pipelineStrategy: PipelineStrategy,
                                private val processStrategy: ProcessStrategy,
                                locationProcessors: List<LocationProcessor>
) : ProcessorsManager {

    private val mapLocationProcessors : Map<String, LocationProcessor> = locationProcessors.associateBy { it.locationName }

    override fun process(allSessions: AllSessions, update: Update): List<BaseRequest<*, *>>? =
        update.message()?.location()?.let { location ->
            update.message().chat().id().let { chatId ->
                allSessions.getSession(chatId).let { session ->
                    pipelineStrategy.pipeline(session, location, null, mapLocationProcessors,
                        "Необходимо прислать локацию") ?:
                    processLocation(session, location)
                }
            }
        }

    private fun processLocation(session: Session, location: Location) : List<BaseRequest<*, *>> =
        processStrategy.process(mapLocationProcessors["find_benches"], session, location, null)

}