package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition

@Processor
class CancelEntityManipulationProcessor : BaseCancelProcessor(
    "cancelEntityManipulation",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION,
    Transition(ProcSpaces.YANDEX_FILE_TREE, "Окей, не будем этого делать", KeyboardService.FILE_TREE_KEYBOARD)
)