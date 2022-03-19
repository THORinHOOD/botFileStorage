package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation

import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseCancelProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class CancelEntityManipulationProcessor : BaseCancelProcessor(
    "file_tree", "?", KeyboardService.FILE_TREE_KEYBOARD
)