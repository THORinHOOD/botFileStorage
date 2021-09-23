package com.geokittens.benchBot.services.bot.processors

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.bot.processors.default_pipeline.WelcomeMessageProcessor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class CancelPipelineProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "default"
        session.currentPipelineInfo.step = "?"
        return listOf(
            SendMessage(session.chatId, "Привет")
            .parseMode(ParseMode.HTML)
            .replyMarkup(WelcomeMessageProcessor.DEFAULT_KEYBOARD))
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.equals(NAME) ?: false

    companion object {
        const val NAME = "Отмена"
    }
}