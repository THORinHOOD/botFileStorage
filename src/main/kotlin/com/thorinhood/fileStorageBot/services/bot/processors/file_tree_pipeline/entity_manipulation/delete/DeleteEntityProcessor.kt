package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.delete

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class DeleteEntityProcessor(
    private val yandexDisk: YandexDisk
) : Processor {

    override val name: String = NAME
    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val inStash = update.message()?.text()?.let { text ->
            when (text) {
                PERMANENTLY -> false
                IN_STASH -> true
                else -> return listOf()
            }
        } ?: return listOf()
        val entityName = session.fileTreeInfo.indexToEntity[session.argument]?.name ?: return listOf(
            SendMessage(session.chatId, "Не удалось удалить")
                .replyMarkup(KeyboardService.FILE_TREE_KEYBOARD)
        )
        val result = yandexDisk.deleteEntity(session.token!!, session.fileTreeInfo.currentPath + entityName,
            !inStash)
        return listOf(if (result) {
            SendMessage(session.chatId, "[$entityName] ${
                if (inStash) {
                    "Перемещено в корзину"
                } else {
                    "Удалено перманентно"
                }}")
        } else {
            SendMessage(session.chatId, "Не удалось удалить $entityName")
        }.replyMarkup(KeyboardService.FILE_TREE_KEYBOARD))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { it == PERMANENTLY || it == IN_STASH } ?: false

    companion object {
        const val NAME = "file_tree_delete"
        const val PERMANENTLY = "Перманентно"
        const val IN_STASH = "Поместить в корзину"
    }
}