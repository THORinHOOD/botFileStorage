package com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.data.FileTreeInfo
import com.thorinhood.fileStorageBot.services.api.FileStorageService
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy

abstract class BaseEntitiesProcessor(
    private val fileStorageService: FileStorageService,
    private val storagePageStrategy: StoragePageStrategy,
    name: String,
    procSpace: String = ""
) : BaseProcessor(
    name,
    procSpace
) {

    protected fun getEntities(session: Session) : ProcessResult {
        val response = fileStorageService.getEntities(
            (session.args["yandex_token"] as String),
            (session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath,
            (session.args["yandex_file_tree_info"] as FileTreeInfo).offset,
            (session.args["yandex_file_tree_info"] as FileTreeInfo).limit)
        return ProcessResult(
            if (response.entities.isEmpty()) {
                null
            } else {
                storagePageStrategy.buildPageWithEntities(response, session, null)
            },
            Transition("file_tree", if (response.entities.isEmpty()) {
                "Объектов в папке [${(session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath}] не найдено \uD83D\uDE1E"
            } else {
                "Переходим к объектам в папке [${(session.args["yandex_file_tree_info"] as FileTreeInfo).currentPath}]"
            }, KeyboardService.FILE_TREE_KEYBOARD))
    }

}