package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseForwardPageProcessor
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import com.thorinhood.fileStorageBot.services.bot.pagination.yandex.YandexEntityPageStrategy

@Processor
class ForwardPageProcessor(
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : BaseForwardPageProcessor<YandexEntity> (
    yandexEntityPageStrategy,
    "forwardPage",
    "file_tree"
)