package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.entity_manipulation.delete

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.api.FileStorageService
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor

@Processor
class DeleteEntityProcessor(
    private val yandexDisk: YandexDisk,
    fileStorageService: FileStorageService,
    storagePageStrategy: StoragePageStrategy
) : BaseEntitiesProcessor(
    fileStorageService,
    storagePageStrategy,
    "deleteEntity",
    "file_tree#entity_manipulation#delete"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val inStash = update.message()?.text()?.let { text ->
            when (text) {
                PERMANENTLY -> false
                IN_STASH -> true
                else -> return ProcessResult.EMPTY_RESULT
            }
        } ?: return ProcessResult.EMPTY_RESULT
        val entityName = session.fileTreeInfo.indexToEntity[session.cursor.args["entity"]]?.name ?:
            return ProcessResult(listOf(SendMessage(session.chatId, "Не удалось удалить")))
        val result = yandexDisk.deleteEntity(session.token!!, session.fileTreeInfo.currentPath + entityName,
            !inStash)
        return getEntities(session).merge(
            ProcessResult(listOf(if (result) {
                 SendMessage(session.chatId, "[$entityName] ${
                     if (inStash) {
                         "Перемещено в корзину"
                     } else {
                         "Удалено перманентно"
                     }}")
             } else {
                 SendMessage(session.chatId, "Не удалось удалить $entityName")
             }))
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, PERMANENTLY) ||
        isUpdateMessageEqualsLabel(update, IN_STASH)

    companion object {
        const val PERMANENTLY = "Перманентно"
        const val IN_STASH = "Поместить в корзину"
    }
}