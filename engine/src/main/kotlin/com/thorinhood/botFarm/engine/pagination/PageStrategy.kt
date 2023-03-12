package com.thorinhood.botFarm.engine.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest

interface PageStrategy<T> {
    fun paginate(callbackQuery: CallbackQuery, sessionId: Long, paginationType: PaginationType): List<BaseRequest<*, *>>
    fun buildPage(response: ElementsResponse<T>, sessionId: Long, callbackId: String?): List<BaseRequest<*, *>>
}