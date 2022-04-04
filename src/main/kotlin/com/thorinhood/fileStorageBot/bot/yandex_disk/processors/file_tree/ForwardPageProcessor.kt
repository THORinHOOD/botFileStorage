package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree

import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexEntity
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseForwardPageProcessor

@Processor
class ForwardPageProcessor(
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : BaseForwardPageProcessor<YandexEntity> (
    yandexEntityPageStrategy,
    "forwardPage",
    ProcSpaces.YANDEX_FILE_TREE
)