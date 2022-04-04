package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.create_folder

import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class CancelCreateFolderProcessor: BaseCancelProcessor(
    "cancelCreateFolder",
    "file_tree#entity_manipulation#create_folder",
    Transition("file_tree", "Окей, не будем этого делать", KeyboardService.FILE_TREE_KEYBOARD)
)
