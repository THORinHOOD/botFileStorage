package com.thorinhood.fileStorageBot.chatBotEngine.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

interface PageStrategy<T : Entity> {
    fun paginate(callbackQuery: CallbackQuery, session: Session, paginationType: PaginationType): List<BaseRequest<*, *>>
    fun buildPage(response: ElementsResponse<T>, session: Session, callbackId: String?): List<BaseRequest<*, *>>
}