package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree

import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseForwardPageProcessor

@Processor
class ForwardPageProcessor(
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : BaseForwardPageProcessor<YandexEntity> (
    yandexEntityPageStrategy,
    "forwardPage",
    ProcSpaces.YANDEX_FILE_TREE
)