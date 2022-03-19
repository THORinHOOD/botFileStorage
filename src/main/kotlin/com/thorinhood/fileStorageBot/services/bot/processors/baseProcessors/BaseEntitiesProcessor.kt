package com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors

import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.FileStorageService
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.pagination.StoragePageStrategy

abstract class BaseEntitiesProcessor(
    private val fileStorageService: FileStorageService,
    private val storagePageStrategy: StoragePageStrategy
) {

    protected fun getEntities(session: Session) : List<BaseRequest<*, *>> {
        val response = fileStorageService.getEntities(
            session.token!!,
            session.fileTreeInfo.currentPath,
            session.fileTreeInfo.offset,
            session.fileTreeInfo.limit)
        session.currentPipelineInfo.pipelineName = "file_tree"
        session.currentPipelineInfo.step = "?"
        return if (response.entities.isEmpty()) {
            listOf(
                SendMessage(
                    session.chatId, "Объектов в папке [${session.fileTreeInfo.currentPath}] не найдено \uD83D\uDE1E"
                ).replyMarkup(KeyboardService.FILE_TREE_KEYBOARD)
            )
        } else {
            storagePageStrategy.buildPageWithEntities(response, session, null)
        }
    }

}