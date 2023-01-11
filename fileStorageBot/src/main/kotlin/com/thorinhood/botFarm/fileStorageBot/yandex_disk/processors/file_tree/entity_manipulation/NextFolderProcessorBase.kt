package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy
import com.thorinhood.botFarm.engine.processors.data.ProcessResult

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class NextFolderProcessorBase(
    yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy,
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "nextFolder",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val folderName = YandexUtils.getContext(session).elementsMap[session.args["entity"]]?.name ?:
            return ProcessResult.EMPTY_RESULT
        YandexUtils.setCurrentPath(session, YandexUtils.getCurrentPath(session) +
            (if (YandexUtils.getCurrentPath(session).endsWith("/")) {
                ""
            } else "/") + "$folderName/"
        )
        YandexUtils.getContext(session).offset = 0
        return getEntities(session)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Перейти в папку"
    }
}