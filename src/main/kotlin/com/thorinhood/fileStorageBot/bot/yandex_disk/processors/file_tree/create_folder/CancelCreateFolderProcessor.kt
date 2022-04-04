package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.create_folder

import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class CancelCreateFolderProcessor: BaseCancelProcessor(
    "cancelCreateFolder",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_CREATE_FOLDER,
    Transition(ProcSpaces.YANDEX_FILE_TREE, "Окей, не будем этого делать", KeyboardService.FILE_TREE_KEYBOARD)
)
