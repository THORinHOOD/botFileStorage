package com.benchinc.benchBot.services.bot.processors.helpers.strategies

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.BaseRequest

interface BenchPageStrategy {
    fun extractMessagePageNumber(message: String) : Int
    fun buildPageWithBenches(session: Session, page: Int, callbackId: String?) : List<BaseRequest<*, *>>
}