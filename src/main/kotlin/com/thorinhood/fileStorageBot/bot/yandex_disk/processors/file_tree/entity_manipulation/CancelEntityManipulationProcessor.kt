package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.entity_manipulation

import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class CancelEntityManipulationProcessor : BaseCancelProcessor(
    "cancelEntityManipulation",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION,
    Transition(ProcSpaces.YANDEX_FILE_TREE, "Окей, не будем этого делать", KeyboardService.FILE_TREE_KEYBOARD)
)