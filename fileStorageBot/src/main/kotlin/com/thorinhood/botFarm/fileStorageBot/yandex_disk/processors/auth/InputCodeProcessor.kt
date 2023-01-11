package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.auth

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.pagination.PaginationContext
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexConst
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.engine.sessions.Session
import org.springframework.stereotype.Service

@Service
@Processor
class InputCodeProcessor(
    private val yandexDisk: YandexDisk,
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "authInputCode",
    ProcSpaces.YANDEX_AUTH
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(update.message()?.text()?.let { message ->
            val token = yandexDisk.getToken(message)
            session.args[YandexConst.TOKEN] = token
            session.args.putIfAbsent(
                YandexConst.PAGINATION_CONTEXT, PaginationContext<YandexEntity>(
                    mutableMapOf("currentPath" to "disk:/")
                )
            )
            listOf(SendMessage(session.sessionId, "Полученный токен: $token")
                .replyMarkup(keyboardService.getDefaultKeyboard(session)))
        } ?: return ProcessResult.EMPTY_RESULT,
        Transition(ProcSpaces.DEFAULT))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)

}