package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseBackPageProcessor
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import com.thorinhood.fileStorageBot.services.bot.pagination.yandex.YandexEntityPageStrategy

@Processor
class BackPageProcessor (
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : BaseBackPageProcessor<YandexEntity>(
    yandexEntityPageStrategy,
    "backPage",
    "file_tree"
)