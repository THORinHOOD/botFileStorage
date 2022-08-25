package com.thorinhood.botFarm.engine.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.sessions.Session

interface PageStrategy<T> {
    fun paginate(callbackQuery: CallbackQuery, session: Session<Long>, paginationType: PaginationType): List<BaseRequest<*, *>>
    fun buildPage(response: ElementsResponse<T>, session: Session<Long>, callbackId: String?): List<BaseRequest<*, *>>
}