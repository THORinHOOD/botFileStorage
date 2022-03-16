package com.thorinhood.fileStorageBot.services.bot.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.fileStorageBot.data.EntitiesListResponse
import com.thorinhood.fileStorageBot.data.Session

interface StoragePageStrategy {
    fun paginate(callbackQuery: CallbackQuery, session: Session, paginationType: PaginationType): List<BaseRequest<*, *>>
    fun buildPageWithEntities(response: EntitiesListResponse, session: Session, callbackId: String?): List<BaseRequest<*, *>>
}