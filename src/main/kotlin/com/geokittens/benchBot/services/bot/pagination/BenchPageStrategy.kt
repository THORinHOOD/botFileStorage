package com.geokittens.benchBot.services.bot.pagination

import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest

interface BenchPageStrategy {
    fun paginate(callbackQuery: CallbackQuery, chatId: Long, paginationType: PaginationType): List<BaseRequest<*, *>>
}