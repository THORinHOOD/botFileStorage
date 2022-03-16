package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions

import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.processors.auth_pipeline.InputCodeProcessor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class StartAuthProcessor(
    private val yandexDisk: YandexDisk
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val link = yandexDisk.getAuthLink()
        session.currentPipelineInfo.pipelineName = "auth"
        session.currentPipelineInfo.step = InputCodeProcessor.NAME
        return listOf(SendMessage(session.chatId, "Перейдите по <a href=\"$link\">ссылке</a> и пришлите код")
            .parseMode(ParseMode.HTML))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.contains("Авторизация") ?: false

    companion object {
        const val NAME = "Авторизация"
    }

}