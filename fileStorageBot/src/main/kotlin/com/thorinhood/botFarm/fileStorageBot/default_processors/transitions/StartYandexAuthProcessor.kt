package com.thorinhood.botFarm.fileStorageBot.default_processors.transitions

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class StartYandexAuthProcessor(
    private val yandexDisk: YandexDisk
) : BaseProcessor(
    "startAuth",
    ProcSpaces.DEFAULT
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        val link = yandexDisk.getAuthLink()
        return ProcessResult(null,
            Transition(ProcSpaces.YANDEX_AUTH, "Перейдите по <a href=\"$link\">ссылке</a> и пришлите код")
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageContainsLabel(update, LABEL)

    companion object {
        const val LABEL = "Авторизация"
    }

}