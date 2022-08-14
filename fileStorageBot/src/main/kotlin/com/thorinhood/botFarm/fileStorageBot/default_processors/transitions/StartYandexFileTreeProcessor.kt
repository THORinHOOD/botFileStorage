package com.thorinhood.botFarm.fileStorageBot.default_processors.transitions

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class StartYandexFileTreeProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
"startFileTree",
    ProcSpaces.DEFAULT
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult = getEntities(session)

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Список файлов"
    }
}