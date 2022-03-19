package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class ShowEntitiesProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy
) : Processor, BaseEntitiesProcessor(yandexDisk, storagePageStrategy) {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return getEntities(session)
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let { it == NAME }  ?: false


    companion object {
        const val NAME = "Показать объекты в текущей папке"
    }
}