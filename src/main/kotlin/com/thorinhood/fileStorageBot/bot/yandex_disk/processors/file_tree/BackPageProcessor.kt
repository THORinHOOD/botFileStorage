package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree

import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexEntity
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseBackPageProcessor

@Processor
class BackPageProcessor (
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : BaseBackPageProcessor<YandexEntity>(
    yandexEntityPageStrategy,
    "backPage",
    ProcSpaces.YANDEX_FILE_TREE
)