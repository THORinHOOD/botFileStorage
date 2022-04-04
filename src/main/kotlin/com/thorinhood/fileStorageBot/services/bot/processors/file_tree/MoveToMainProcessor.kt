package com.thorinhood.fileStorageBot.services.bot.processors.file_tree

import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.yandex.YandexUtils

@Processor
class MoveToMainProcessor(
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "moveToMain",
    "file_tree"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        YandexUtils.getContext(session).context["currentPath"] = "disk:/"
        return ProcessResult(
            null,
            Transition("default", "Возвращаемся на главную страницу", keyboardService.getDefaultKeyboard(session))
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Главная"
    }
}