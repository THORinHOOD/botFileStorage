package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor

import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class GetFileProcessor : BaseProcessor(
    "getFile",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        val entity = YandexUtils.getContext(session).elementsMap[session.cursor.args["entity"]] ?:
            return ProcessResult.EMPTY_RESULT
        return ProcessResult(
            null,
            Transition(
                ProcSpaces.YANDEX_FILE_TREE, "<a href=\"${entity.href}\">Ссылка</a> для скачивания файла",
                KeyboardService.FILE_TREE_KEYBOARD))
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Скачать файл"
    }
}