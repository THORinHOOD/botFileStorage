package com.thorinhood.fileStorageBot.services.bot.processors

import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.WelcomeMessageProcessor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import org.springframework.stereotype.Service

@Service
class CancelPipelineProcessor(
    private val keyboardService: KeyboardService
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "default"
        session.currentPipelineInfo.step = "?"
        return listOf(
            SendMessage(session.chatId, "Привет")
            .parseMode(ParseMode.HTML)
            .replyMarkup(keyboardService.getDefaultKeyboard(session)))
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.equals(NAME) ?: false

    companion object {
        const val NAME = "Отмена"
    }
}