package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.delete

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
class StartDeleteEntityProcessor : Processor {

    override val name: String = NAME
    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return listOf(SendMessage(session.chatId, "Удалить перманентно или переместить в корзину?")
            .replyMarkup(KeyboardService.FILE_TREE_DELETE_CHOICE))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { it == NAME } ?: false

    companion object {
        const val NAME = "Удалить"
    }
}