package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline

import com.fasterxml.jackson.core.sym.Name
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class GetFileProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val entity = session.indexToEntity[update.message().text()] ?: return listOf()
        return listOf(SendMessage(session.chatId, "<a href=\"${entity.href}\">Ссылка</a> для скачивания файла")
            .parseMode(ParseMode.HTML))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { text ->
            text.startsWith("/") && session.indexToEntity.contains(text) &&
                    session.indexToEntity[text]?.type == "file"
        } ?: false

    companion object {
        const val NAME = "file_tree_get_file"
    }
}