package com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.processors.Processor

abstract class BaseCancelProcessor(
    private val pipelineName: String,
    private val step: String,
    private val replyKeyboardMarkup: ReplyKeyboardMarkup
) : Processor {

    override val name = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = pipelineName
        session.currentPipelineInfo.step = step
        return listOf(
            SendMessage(session.chatId, "Окей, не будем этого делать")
                .replyMarkup(replyKeyboardMarkup)
        )
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let {
            it == NAME
        } ?: false

    companion object {
        const val NAME = "Отмена"
    }

}