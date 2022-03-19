package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class GetFileProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val entity = session.fileTreeInfo.indexToEntity[session.argument] ?: return listOf()
        return listOf(SendMessage(session.chatId, "<a href=\"${entity.href}\">Ссылка</a> для скачивания файла")
            .parseMode(ParseMode.HTML).replyMarkup(KeyboardService.FILE_TREE_KEYBOARD))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { it == NAME } ?: false

    companion object {
        const val NAME = "Скачать файл"
    }
}