package com.geokittens.benchBot.services.bot.processors.add_bench_pipeline

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.RequestsServiceLocal
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import com.geokittens.benchBot.services.bot.processors.default_pipeline.WelcomeMessageProcessor
import com.google.protobuf.ByteString
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.benchLib.proto.common.Location
import com.thorinhood.benchLib.proto.services.AddBenchRequest
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import org.springframework.stereotype.Service

@Service
@Pipeline("add_bench")
class AddBenchPhotoProcessor(
    private val benchServiceStub: BenchServiceGrpc.BenchServiceBlockingStub,
    private val requestsServiceLocal: RequestsServiceLocal,
    private val telegramBot: TelegramBot
) : Processor {
    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.message()?.photo()?.let { arrayOfPhotoSizes ->
            arrayOfPhotoSizes.toList().maxByOrNull { it.fileSize() }?.let { photoSize ->
                val response = telegramBot.execute(GetFile(photoSize.fileId()))
                val photo = telegramBot.getFileContent(response.file())
                val currentRequest = requestsServiceLocal.getRequest(session.chatId)
                benchServiceStub.addBench(AddBenchRequest.newBuilder()
                    .setLocation(Location.newBuilder()
                        .setLon(currentRequest.location.coordinates[0])
                        .setLat(currentRequest.location.coordinates[1])
                        .build())
                    .setPhoto(ByteString.copyFrom(photo))
                    .build())
                session.currentPipelineInfo.pipelineName = "default"
                session.currentPipelineInfo.step = "?"
                listOf(
                    SendMessage(session.chatId, "Лавочка добавлена!")
                        .replyMarkup(WelcomeMessageProcessor.DEFAULT_KEYBOARD)
                )
            }
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.photo() != null && update.message()?.photo()?.isNotEmpty() ?: false

    companion object {
        const val NAME = "add_photo"
    }
}