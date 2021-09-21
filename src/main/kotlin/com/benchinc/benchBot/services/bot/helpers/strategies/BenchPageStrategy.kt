package com.benchinc.benchBot.services.bot.helpers.strategies

import com.db.benchLib.data.bench.BenchesNearResponse
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.benchLib.proto.services.FindBenchesNearResponse

interface BenchPageStrategy {
    fun extractMessagePageNumber(message: String) : Int
    fun buildPageWithBenches(findBenchesNearResponse: FindBenchesNearResponse, chatId : Long, callbackId: String?) : List<BaseRequest<*, *>>
    fun withCallbackData(callbackQuery: CallbackQuery,
                         chatId: Long,
                         dataProcessor: (Long, String, Int, Int, Double, Double, Double) -> List<BaseRequest<*, *>>): List<BaseRequest<*, *>>
}