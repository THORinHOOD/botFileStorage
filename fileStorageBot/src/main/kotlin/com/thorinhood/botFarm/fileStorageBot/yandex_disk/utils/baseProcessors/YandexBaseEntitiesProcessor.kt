package com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.baseProcessors

import com.thorinhood.botFarm.engine.pagination.PaginationContext
import com.thorinhood.botFarm.engine.processors.data.Transition

import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexConst
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.botFarm.fileStorageBot.extensions.BaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk

abstract class YandexBaseEntitiesProcessor(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy,
    name: String,
    procSpace: String = "",
) : BaseEntitiesProcessor<YandexEntity>(
    yandexDisk,
    yandexEntityPageStrategy,
    YandexConst.TOKEN,
    name,
    procSpace
) {

    override fun getContext(session: Session): PaginationContext<YandexEntity> =
        YandexUtils.getContext(session)

    override fun buildTransition(
        hasEntities: Boolean,
        extraArg: String
    ) = Transition(
        ProcSpaces.YANDEX_FILE_TREE, if (hasEntities) {
            "Объектов в папке [$extraArg] не найдено \uD83D\uDE1E"
        } else {
            "Переходим к объектам в папке [$extraArg]"
        }, KeyboardService.FILE_TREE_KEYBOARD)

}