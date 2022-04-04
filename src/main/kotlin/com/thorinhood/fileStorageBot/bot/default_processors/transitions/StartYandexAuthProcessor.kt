package com.thorinhood.fileStorageBot.bot.default_processors.transitions

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.pengrad.telegrambot.model.Update
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition

@Processor
class StartYandexAuthProcessor(
    private val yandexDisk: YandexDisk
) : BaseProcessor(
    "startAuth",
    ProcSpaces.DEFAULT
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val link = yandexDisk.getAuthLink()
        return ProcessResult(null,
            Transition(ProcSpaces.YANDEX_AUTH, "Перейдите по <a href=\"$link\">ссылке</a> и пришлите код")
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Авторизация"
    }

}