package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.create_folder

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.data.FileTreeInfo
import com.thorinhood.fileStorageBot.services.api.FileStorageService
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseEntitiesProcessor

@Processor
class CreateFolderProcessor(
    private val yandexDisk: YandexDisk,
    fileStorageService: FileStorageService,
    storagePageStrategy: StoragePageStrategy
) : BaseEntitiesProcessor(
    fileStorageService,
    storagePageStrategy,
    "createFolder",
    "file_tree#entity_manipulation#create_folder"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult =
        getEntities(session).merge(ProcessResult(update.message()?.text()?.let { folderName ->
            val folderPath = "${(session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath}$folderName/"
            val created = yandexDisk.createFolder(session.args["yandex_token"] as String, folderPath)
            listOf(SendMessage(session.chatId, if (created) {
                "Создана папка [$folderPath]"
            } else {
                "Не удалось создать папку [$folderName]"
            }))
        } ?: listOf(),
        Transition("file_tree")))

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)

}