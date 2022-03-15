package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class MoveToMainProcessor(
    private val keyboardService: KeyboardService
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPath = "disk:/"
        session.currentPipelineInfo.pipelineName = "default"
        session.currentPipelineInfo.step = "?"
        return listOf(SendMessage(session.chatId, "Возвращаемся на главную страницу")
            .replyMarkup(keyboardService.getDefaultKeyboard(session)))
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    companion object {
        const val NAME = "Главная"
    }
}