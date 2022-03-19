package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.create_folder

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class CreateFolderProcessor(
    private val yandexDisk: YandexDisk
) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        update.message()?.text()?.let { folderName ->
            val folderPath = "${session.fileTreeInfo.currentPath}$folderName/"
            val created = yandexDisk.createFolder(session.token!!, folderPath)
            session.currentPipelineInfo.pipelineName = "file_tree"
            session.currentPipelineInfo.step = "?"
            return listOf(SendMessage(session.chatId, if (created) {
                "Создана папка [$folderPath]"
            } else {
                "Не удалось создать папку [$folderName]"
            }).replyMarkup(KeyboardService.FILE_TREE_KEYBOARD))
        } ?: listOf()

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        session.currentPipelineInfo.step == NAME

    companion object {
        const val NAME = "file_tree_create_folder"
    }

}