package com.benchinc.benchBot.services.bot.helpers.strategies

import com.benchinc.benchBot.services.bot.processors.default_pipeline.BackPageProcessor
import com.benchinc.benchBot.services.bot.processors.default_pipeline.ForwardPageProcessor
import com.db.benchLib.data.bench.BenchesNearResponse
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class BenchPageStrategyImpl(private val benchInfoStrategy: BenchInfoStrategy) : BenchPageStrategy {

    override fun extractMessagePageNumber(message: String) : Int =
        message.substring(message.indexOf("(") + 1, message.indexOf("/")).toInt() - 1

    override fun buildPageWithBenches(benchesNearResponse: BenchesNearResponse, chatId: Long, callbackId: String?) : List<BaseRequest<*, *>> {
        benchesNearResponse.page.let { page ->
            benchesNearResponse.pagesCount.let { pagesCount ->
                benchesNearResponse.pageSize.let { pageSize ->
                    val result = buildString {
                        append("(${page + 1}/${pagesCount}) " +
                                "Найдено <b>${benchesNearResponse.benchesCount}</b> лавочек в радиусе " +
                                "<b>${(benchesNearResponse.radius * 1000).toInt()}</b> метров\n\n")
                        for ((index, value) in benchesNearResponse.benches.withIndex()) {
                            val realIndex = index + 1 + page * pageSize
                            append("<b>${realIndex}.</b> ${benchInfoStrategy.description(value)} \nПоказать на карте:\n/bench_${value.id}\n\n")
                        }
                    }
                    val responses = mutableListOf<BaseRequest<*, *>>()
                    callbackId?.let {
                        responses.add(AnswerCallbackQuery(it))
                    }

                    val pageInfo = buildCurrentPageInfo(
                        page,
                        pageSize,
                        benchesNearResponse.lat,
                        benchesNearResponse.lon,
                        benchesNearResponse.radius
                    )
                    responses.add(SendMessage(chatId, result)
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(
                            InlineKeyboardMarkup(
                                InlineKeyboardButton("Назад").callbackData(
                                    BackPageProcessor.NAME + "_" + if (page == 0) "stop" else pageInfo
                                ),
                                InlineKeyboardButton("Дальше").callbackData(
                                    ForwardPageProcessor.NAME + "_" +
                                            if (page + 1 == pagesCount) "stop" else pageInfo
                                )
                            )
                        ))
                    return responses
                }
            }
        }
    }

    override fun withCallbackData(callbackQuery: CallbackQuery,
                                  chatId: Long,
                                  dataProcessor: (Long, String, Int, Int, Double, Double, Double) -> List<BaseRequest<*, *>>)
                 : List<BaseRequest<*, *>> {
        val data = callbackQuery.data().split("_")
        if (data[1] == "stop") {
            return listOf(AnswerCallbackQuery(callbackQuery.id()))
        }
        return dataProcessor.invoke(
            chatId,
            callbackQuery.id(),
            data[1].toInt(),
            data[2].toInt(),
            data[3].toDouble(),
            data[4].toDouble(),
            data[5].toDouble()
        )
    }

    private fun buildCurrentPageInfo(page: Int, pageSize: Int, lat: Double, lon: Double, radius: Double) : String {
        return "${page}_${pageSize}_${lat}_${lon}_${radius}"
    }

}