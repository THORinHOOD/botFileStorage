package com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
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
            session.token!!,
            session.fileTreeInfo.currentPath,
            session.fileTreeInfo.offset,
            session.fileTreeInfo.limit)
        return ProcessResult(
            if (response.entities.isEmpty()) {
                null
            } else {
                storagePageStrategy.buildPageWithEntities(response, session, null)
            },
            Transition("file_tree", if (response.entities.isEmpty()) {
                "Объектов в папке [${session.fileTreeInfo.currentPath}] не найдено \uD83D\uDE1E"
            } else {
                "Переходим к объектам в папке [${session.fileTreeInfo.currentPath}]"
            }, KeyboardService.FILE_TREE_KEYBOARD))
    }

}