package com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class StartYandexAuthProcessor(
    private val yandexDisk: YandexDisk
) : BaseProcessor(
    "startAuth",
    "default"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val link = yandexDisk.getAuthLink()
        return ProcessResult(null,
            Transition("auth", "Перейдите по <a href=\"$link\">ссылке</a> и пришлите код")
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Авторизация"
    }

}