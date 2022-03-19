package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.create_folder

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class StartCreateFolderProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "file_tree"
        session.currentPipelineInfo.step = CreateFolderProcessor.NAME
        return listOf(SendMessage(session.chatId, "Напишите имя папки\nПапка будет создана внутри текущей папки " +
                "[${session.fileTreeInfo.currentPath}]")
            .replyMarkup(KeyboardService.FILE_TREE_CREATE_FOLDER_KEYBOARD))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        update.message()?.text()?.let {
            it == NAME
        } ?: false

    companion object {
        const val NAME = "Создать папку"
    }

}