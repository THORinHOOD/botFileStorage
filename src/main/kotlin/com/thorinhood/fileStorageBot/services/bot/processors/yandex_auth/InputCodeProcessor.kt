package com.thorinhood.fileStorageBot.services.bot.processors.yandex_auth

import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import org.springframework.stereotype.Service

@Service
@Processor
class InputCodeProcessor(
    private val yandexDisk: YandexDisk,
    private val keyboardService: KeyboardService
) : BaseProcessor(
    "authInputCode",
    "auth"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        return ProcessResult(update.message()?.text()?.let { message ->
            val token = yandexDisk.getToken(message)
            session.args["yandex_token"] = token
            session.args.putIfAbsent(
                "yandex_pagination_context", PaginationContext<YandexEntity>(
                    mutableMapOf("currentPath" to "disk:/")
                )
            )
            listOf(SendMessage(session.chatId, "Полученный токен: $token")
                .replyMarkup(keyboardService.getDefaultKeyboard(session)))
        } ?: return ProcessResult.EMPTY_RESULT,
        Transition("default"))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)

}