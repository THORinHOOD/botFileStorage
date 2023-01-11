package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils

@Processor
class StartCreateFolderProcessor : BaseProcessor(
    "startCreateFolder",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(
            listOf(SendMessage(session.sessionId, "Напишите имя папки\nПапка будет создана внутри текущей папки " +
                "[${YandexUtils.getCurrentPath(session)}]")
            .replyMarkup(KeyboardService.FILE_TREE_CREATE_FOLDER_KEYBOARD)),
            Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_CREATE_FOLDER))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Создать папку"
    }
}