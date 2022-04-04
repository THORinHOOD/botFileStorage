package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.pagination.yandex.YandexEntityPageStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.fileStorageBot.services.bot.yandex.YandexUtils

@Processor
class NextFolderProcessorBase(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy,
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "nextFolder",
    "file_tree#entity_manipulation"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val folderName = YandexUtils.getContext(session).elementsMap[session.cursor.args["entity"]]?.name ?:
            return ProcessResult.EMPTY_RESULT
        YandexUtils.getContext(session).context["currentPath"] =
            YandexUtils.getContext(session).context["currentPath"] as String +
                (if ((YandexUtils.getContext(session).context["currentPath"] as String).endsWith("/")) {
                    ""
                } else "/") + "$folderName/"
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Перейти в папку"
    }
}