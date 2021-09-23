package com.geokittens.benchBot.services.bot.processors.default_pipeline

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.bot.description.BenchDescriptionStrategy
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendPhoto
import com.thorinhood.benchLib.proto.common.FindByStringRequest
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import io.prometheus.client.Counter
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class GetBenchProcessor(
    private val benchServiceStub: BenchServiceGrpc.BenchServiceBlockingStub,
    private val requestsCounter: Counter,
    private val benchDescriptionStrategy: BenchDescriptionStrategy
) : Processor, Logging {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.text()?.let { text ->
            requestsCounter.labels("get_bench").inc()
            val benchId = text.substring(7)

            val bench = benchServiceStub.getBenchById(
                FindByStringRequest.newBuilder()
                    .setId(benchId)
                    .build()
            )
            val messages: MutableList<BaseRequest<*, *>> = mutableListOf()

            if (bench.photo != null && !bench.photo.isEmpty) {
                messages.add(
                    SendPhoto(session.chatId, bench.photo.toByteArray())
                        .caption(benchDescriptionStrategy.description(bench.benchInfo))
                )
            } else {
                messages.add(
                    SendMessage(
                        session.chatId, "Нет фотографии\n" + benchDescriptionStrategy.description(bench.benchInfo)
                    )
                )
            }
            messages.add(
                SendLocation(
                    session.chatId,
                    bench.benchInfo.location.lat.toFloat(),
                    bench.benchInfo.location.lon.toFloat()
                )
            )
            return messages
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    companion object {
        const val NAME = "bench"
    }
}