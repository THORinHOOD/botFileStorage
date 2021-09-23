package com.geokittens.benchBot.services.bot.pagination

import com.geokittens.benchBot.services.bot.description.BenchDescriptionStrategy
import com.geokittens.benchBot.services.bot.processors.default_pipeline.BackPageProcessor
import com.geokittens.benchBot.services.bot.processors.default_pipeline.ForwardPageProcessor
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.benchLib.proto.common.Location
import com.thorinhood.benchLib.proto.common.PageInfo
import com.thorinhood.benchLib.proto.common.PaginationInfo
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc
import com.thorinhood.benchLib.proto.services.FindBenchesNearRequest
import com.thorinhood.benchLib.proto.services.FindBenchesNearResponse
import org.springframework.stereotype.Service
import java.util.function.Predicate

@Service
class BenchPageStrategyImpl(
    private val benchDescriptionStrategy: BenchDescriptionStrategy,
    private val benchServiceStub: BenchServiceGrpc.BenchServiceBlockingStub
) : BenchPageStrategy {

    override fun paginate(
        callbackQuery: CallbackQuery,
        chatId: Long,
        paginationType: PaginationType
    ): List<BaseRequest<*, *>> {
        val data = callbackQuery.data().split("_")
        if (data[1] == "stop") {
            return listOf(AnswerCallbackQuery(callbackQuery.id()))
        }
        val pageCallback = PageCallback.fromList(data)
        val response = benchServiceStub.findBenchesNear(
            FindBenchesNearRequest.newBuilder()
                .setLocation(
                    Location.newBuilder()
                        .setLon(pageCallback.lon)
                        .setLat(pageCallback.lat)
                        .build()
                )
                .setPageInfo(
                    PageInfo.newBuilder()
                        .setIndex(
                            when (paginationType) {
                                PaginationType.FORWARD -> pageCallback.page + 1
                                PaginationType.BACK -> pageCallback.page - 1
                            }
                        )
                        .setSize(pageCallback.pageSize)
                        .build()
                )
                .setRadius(pageCallback.radius)
                .build()
        )

        return if (response.paginationInfo.elementsCount == 0) {
            listOf(AnswerCallbackQuery(callbackQuery.id()))
        } else {
            buildPageWithBenches(response, chatId, callbackQuery.id())
        }
    }

    private fun buildPageWithBenches(
        response: FindBenchesNearResponse,
        chatId: Long,
        callbackId: String?
    ): List<BaseRequest<*, *>> {
        val paginationInfo = response.paginationInfo

        val result = buildString {
            append(
                "(${paginationInfo.pageInfo.index + 1}/${paginationInfo.pagesCount}) " +
                        "Найдено <b>${paginationInfo.elementsCount}</b> лавочек в радиусе " +
                        "<b>${(response.radius * 1000).toInt()}</b> метров\n\n"
            )
            for ((index, value) in response.benchesList.withIndex()) {
                val realIndex = index + 1 + paginationInfo.pageInfo.index * paginationInfo.pageInfo.size
                append(
                    "<b>${realIndex}.</b> ${benchDescriptionStrategy.description(value)} \nПоказать на карте:\n" +
                            "/bench_${value.benchInfo.id}\n\n"
                )
            }
        }
        val responses = mutableListOf<BaseRequest<*, *>>()
        callbackId?.let {
            responses.add(AnswerCallbackQuery(it))
        }

        val pageCallback = PageCallback(
            paginationInfo.pageInfo.index,
            paginationInfo.pageInfo.size,
            response.location.lat,
            response.location.lon,
            response.radius
        )

        responses.add(
            SendMessage(chatId, result)
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
                    paginationInfo.pageInfo.index + 1 == paginationInfo.pagesCount
                }
            )

    private fun backButton(paginationInfo: PaginationInfo, pageCallback: PageCallback): InlineKeyboardButton =
        InlineKeyboardButton("Назад")
            .callbackData(
                buttonInfo(BackPageProcessor.NAME, pageCallback, paginationInfo) {
                    it.pageInfo.index == 0
                }
            )

    private fun buttonInfo(
        name: String, pageCallback: PageCallback, paginationInfo: PaginationInfo,
        isStop: Predicate<PaginationInfo>
    ): String =
        name + "_" + if (isStop.test(paginationInfo)) {
            "stop"
        } else {
            pageCallback.buildCallbackInfo()
        }
}