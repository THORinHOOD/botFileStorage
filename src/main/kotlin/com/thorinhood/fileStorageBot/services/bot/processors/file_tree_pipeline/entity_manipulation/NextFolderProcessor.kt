package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class NextFolderProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : Processor, BaseEntitiesProcessor(yandexDisk, storagePageStrategy) {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val folderName = session.fileTreeInfo.indexToEntity[session.argument]?.name ?: return listOf()
        session.fileTreeInfo.currentPath = session.fileTreeInfo.currentPath +
                (if (session.fileTreeInfo.currentPath.endsWith("/")) {
                    ""
                } else "/") + "$folderName/"
        val responses = mutableListOf<BaseRequest<*, *>>()
        responses.add(SendMessage(session.chatId, "Переходим в папку $folderName")
            .replyMarkup(KeyboardService.FILE_TREE_KEYBOARD))
        responses.addAll(getEntities(session))
        return responses
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { it == NAME } ?: false

    companion object {
        const val NAME = "Перейти в папку"
    }

}