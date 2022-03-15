package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions

import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class StartEntitiesProcessor(
    yandexDisk: YandexDisk,
    storagePageStrategy: StoragePageStrategy,
    private val requestsCounter: Counter
) : Processor, BaseEntitiesProcessor(yandexDisk, storagePageStrategy) {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
//        requestsCounter.labels("find_benches").inc()
        return getEntities(session, 0, 10)
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(NAME) ?: false

    companion object {
        const val NAME = "Список файлов"
    }

}