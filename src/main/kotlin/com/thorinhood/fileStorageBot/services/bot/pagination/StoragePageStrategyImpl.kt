package com.thorinhood.fileStorageBot.services.bot.pagination

import com.thorinhood.fileStorageBot.services.bot.description.EntityDescriptionStrategy
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.BackPageProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.ForwardPageProcessor
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.EntitiesListResponse
import com.thorinhood.fileStorageBot.data.Entity
import com.thorinhood.fileStorageBot.data.PaginationInfo
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import org.springframework.stereotype.Service
import java.util.function.Predicate
import kotlin.math.roundToInt

@Service
class StoragePageStrategyImpl(
    private val entityDescriptionStrategy: EntityDescriptionStrategy,
    private val yandexDisk: YandexDisk,
    private val keyboardService: KeyboardService
) : StoragePageStrategy {

    override fun paginate(
        callbackQuery: CallbackQuery,
        session: Session,
        paginationType: PaginationType
    ): List<BaseRequest<*, *>> {
        val data = callbackQuery.data().split("_")
        if (data[1] == "stop") {
            return listOf(AnswerCallbackQuery(callbackQuery.id()))
        }

        val pageCallback = PageCallback.fromList(data)

        val response = yandexDisk.getEntities(session.token!!, session.currentPath,
            when (paginationType) {
                PaginationType.FORWARD -> pageCallback.page + 1
                PaginationType.BACK -> pageCallback.page - 1
            } * pageCallback.pageSize, pageCallback.pageSize)

        return if (response.entities.isEmpty()) {
            listOf(AnswerCallbackQuery(callbackQuery.id()))
        } else {
            buildPageWithEntities(response, session, callbackQuery.id())
        }
    }

    override fun buildPageWithEntities(
        response: EntitiesListResponse,
        session: Session,
        callbackId: String?
    ): List<BaseRequest<*, *>> {
        var pagesCount = (response.total.toDouble()/response.limit).roundToInt()
        if (pagesCount == 0) {
            pagesCount = 1
        }
        val currentPage = response.offset/response.limit + 1
        val paginationInfo = PaginationInfo(currentPage - 1, pagesCount)
        session.indexToEntity.clear()
        val result = buildString {
            append(
                "Объектов в папке [<b>${response.path}</b>] : <b>${response.total}</b>\n" +
                "(${currentPage}/${pagesCount})\n\n"
            )
            for ((index, value) in response.entities.withIndex()) {
                val realIndex = index + 1 + (currentPage - 1) * response.limit
                append(
                    "${entityDescriptionStrategy.description(realIndex, value)} \n"
                )
                session.indexToEntity["/${realIndex}"] = value
            }
        }
        val responses = mutableListOf<BaseRequest<*, *>>()
        callbackId?.let {
            responses.add(AnswerCallbackQuery(it))
        }

        val pageCallback = PageCallback(currentPage - 1, response.limit)

        responses.add(
            SendMessage(session.chatId, result)
                .parseMode(ParseMode.HTML)
                .replyMarkup(
                    InlineKeyboardMarkup(
                        backButton(paginationInfo, pageCallback),
                        forwardButton(paginationInfo, pageCallback)
                    )
                )
        )
        return responses
    }

    private fun forwardButton(paginationInfo: PaginationInfo, pageCallback: PageCallback): InlineKeyboardButton =
        InlineKeyboardButton("Дальше")
            .callbackData(
                buttonInfo(ForwardPageProcessor.NAME, pageCallback, paginationInfo) {
                    paginationInfo.currentPage + 1 == paginationInfo.totalPages
                }
            )

    private fun backButton(paginationInfo: PaginationInfo, pageCallback: PageCallback): InlineKeyboardButton =
        InlineKeyboardButton("Назад")
            .callbackData(
                buttonInfo(BackPageProcessor.NAME, pageCallback, paginationInfo) {
                    it.currentPage == 0
                }
            )

    private fun buttonInfo(
        name: String,
        pageCallback: PageCallback,
        paginationInfo: PaginationInfo,
        isStop: Predicate<PaginationInfo>
    ): String =
        name + "_" + if (isStop.test(paginationInfo)) {
            "stop"
        } else {
            pageCallback.buildCallbackInfo()
        }
}