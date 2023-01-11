package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils

import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class MoveToMainProcessor(
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "moveToMain",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        YandexUtils.setCurrentPath(session, "disk:/")
        return ProcessResult(
            null,
            Transition(ProcSpaces.DEFAULT, "Возвращаемся на главную страницу", keyboardService.getDefaultKeyboard(session))
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Главная"
    }
}