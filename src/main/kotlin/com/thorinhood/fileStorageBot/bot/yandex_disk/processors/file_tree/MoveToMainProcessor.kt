package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils

import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

@Processor
class MoveToMainProcessor(
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "moveToMain",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        YandexUtils.setCurrentPath(session, "disk:/")
        return ProcessResult(
            null,
            Transition(ProcSpaces.DEFAULT, "Возвращаемся на главную страницу", keyboardService.getDefaultKeyboard(session))
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Главная"
    }
}