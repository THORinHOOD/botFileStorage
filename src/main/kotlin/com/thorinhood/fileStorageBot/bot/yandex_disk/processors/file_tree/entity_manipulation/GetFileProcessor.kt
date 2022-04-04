package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils

@Processor
class GetFileProcessor : BaseProcessor(
    "getFile",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION
) {

    override fun processInner(
        session: Session,
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

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Скачать файл"
    }
}