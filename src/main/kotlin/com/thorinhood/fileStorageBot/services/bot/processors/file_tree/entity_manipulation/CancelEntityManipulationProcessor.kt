package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.entity_manipulation

import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class CancelEntityManipulationProcessor : BaseCancelProcessor(
    "cancelEntityManipulation",
    "file_tree#entity_manipulation",
    Transition("file_tree", "Окей, не будем этого делать", KeyboardService.FILE_TREE_KEYBOARD)
)