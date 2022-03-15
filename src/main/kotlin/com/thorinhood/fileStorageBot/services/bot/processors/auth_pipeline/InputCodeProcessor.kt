package com.thorinhood.fileStorageBot.services.bot.processors.auth_pipeline

import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import org.springframework.stereotype.Service

@Service
@Pipeline("auth")
class InputCodeProcessor(
    private val yandexDisk: YandexDisk,
    private val keyboardService: KeyboardService
) : Processor {
    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.message()?.text()?.let { message ->
            val token = yandexDisk.getToken(message)
            session.currentPipelineInfo.pipelineName = "default"
            session.currentPipelineInfo.step = "?"
            session.token = token
            listOf(SendMessage(session.chatId, "Полученный токен: $token")
                .replyMarkup(keyboardService.getDefaultKeyboard(session)))
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        true

    companion object {
        const val NAME = "auth_input_code"
    }

}