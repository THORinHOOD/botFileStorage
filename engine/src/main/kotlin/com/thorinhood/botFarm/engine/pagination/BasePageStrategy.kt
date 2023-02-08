package com.thorinhood.botFarm.engine.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.data.Session

import java.util.function.Predicate
import kotlin.math.roundToInt

abstract class BasePageStrategy<T>(
    private val elementDescriptionStrategy: ElementDescriptionStrategy<T>
): PageStrategy<T> {

    override fun paginate(
        callbackQuery: CallbackQuery,
        session: Session,
        paginationType: PaginationType
    ): List<BaseRequest<*, *>> {
        val data = callbackQuery.data().split("_")
        if (data[1] == "stop") {
            return listOf(AnswerCallbackQuery(callbackQuery.id()))
        }
        val response = getElements(session, PageCallback.fromList(data), paginationType)
        return if (response.entities.isEmpty()) {
            listOf(AnswerCallbackQuery(callbackQuery.id()))
        } else {
            buildPage(response, session, callbackQuery.id())
        }
    }

    override fun buildPage(
        response: ElementsResponse<T>,
        session: Session,
        callbackId: String?
    ): List<BaseRequest<*, *>> {
        var pagesCount = (response.total.toDouble()/response.limit).roundToInt()
        if (pagesCount == 0) {
            pagesCount = 1
        }
        val currentPage = response.offset/response.limit + 1
        val paginationInfo =
            PaginationInfo(currentPage - 1, pagesCount)
        val paginationContext = paginationContextExtract(session)
        paginationContext.elementsMap.clear()
        val result = buildString {
            append(
                "Объектов в папке [<b>${response.path}</b>] : <b>${response.total}</b>\n" +
                        "(${currentPage}/${pagesCount})\n\n"
            )
            for ((index, value) in response.entities.withIndex()) {
                val realIndex = index + 1 + (currentPage - 1) * response.limit
                append(
                    "${elementDescriptionStrategy.description(realIndex, value)} \n"
                )
                paginationContext.elementsMap["/${realIndex}"] = value
            }
        }
        val responses = mutableListOf<BaseRequest<*, *>>()
        callbackId?.let {
            responses.add(AnswerCallbackQuery(it))
        }

        val pageCallback = PageCallback(currentPage - 1, response.limit)

        responses.add(
            SendMessage(session.sessionId, result)
                .parseMode(ParseMode.HTML)
                .replyMarkup(
                    InlineKeyboardMarkup(
                        backButton(paginationInfo, pageCallback),
                        forwardButton(paginationInfo, pageCallback)
                    )
                )
        )
        paginationContext.offset = response.offset
        paginationContext.limit = response.limit
        return responses
    }

    protected abstract fun paginationContextExtract(session: Session) : PaginationContext<T>
    protected abstract fun getElements(session: Session, pageCallback: PageCallback, paginationType: PaginationType) : ElementsResponse<T>

    private fun forwardButton(paginationInfo: PaginationInfo, pageCallback: PageCallback): InlineKeyboardButton =
        InlineKeyboardButton("Дальше")
            .callbackData(
                buttonInfo("forward", pageCallback, paginationInfo) {
                    paginationInfo.currentPage + 1 == paginationInfo.totalPages
                }
            )

    private fun backButton(paginationInfo: PaginationInfo, pageCallback: PageCallback): InlineKeyboardButton =
        InlineKeyboardButton("Назад")
            .callbackData(
                buttonInfo("back", pageCallback, paginationInfo) {
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

    data class PaginationInfo (
        val currentPage: Int,
        val totalPages: Int
    )
}