package com.benchinc.benchBot.services.bot.processors.add_bench_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.RequestsServiceLocal
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.benchinc.benchBot.services.bot.processors.default_pipeline.WelcomeMessageProcessor
import com.db.benchLib.clients.RequestsServiceClient
import com.db.benchLib.data.request.NewRequestDto
import com.db.benchLib.services.MultipartFileService
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("add_bench")
class AddBenchPhotoProcessor(private val multipartFileService: MultipartFileService,
                             private val requestsServiceClient: RequestsServiceClient,
                             private val requestsServiceLocal: RequestsServiceLocal,
                             private val telegramBot: TelegramBot) : Processor {
    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.message()?.photo()?.let { arrayOfPhotoSizes ->
            arrayOfPhotoSizes.toList().maxByOrNull { it.fileSize() }?.let { photoSize ->
                val response = telegramBot.execute(GetFile(photoSize.fileId()))
                val photo = telegramBot.getFileContent(response.file())

                val photoMultipart = multipartFileService.buildFromPhoto(photo)
                val request = requestsServiceLocal.getRequest(session.chatId)
                requestsServiceClient.addRequest(photoMultipart, NewRequestDto.builder()
                    .location(request.location)
                    .properties(mapOf())
                    .build())

                session.currentPipelineInfo.pipelineName = "default"
                session.currentPipelineInfo.step = "?"
                listOf(
                    SendMessage(session.chatId, "Заявка на добавление лавочки сохранена!")
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