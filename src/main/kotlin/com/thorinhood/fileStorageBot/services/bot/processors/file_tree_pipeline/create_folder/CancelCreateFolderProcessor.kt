package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.create_folder

import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseCancelProcessor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class CancelCreateFolderProcessor: BaseCancelProcessor("file_tree", "?", KeyboardService.FILE_TREE_KEYBOARD)
