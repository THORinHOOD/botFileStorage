package com.benchinc.benchBot.services.bot.processors.helpers

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class BenchPageStrategy {

    fun extractMessagePageNumber(message: String) : Int =
        message.substring(message.indexOf("(") + 1, message.indexOf("/")).toInt() - 1

    fun buildPageWithBenches(session: Session, page: Int, callbackId: String?) : List<BaseRequest<*, *>> {
        session.currentBenches.let { benches ->
            val pagesCount = benches.size / 5 + (if (benches.size % 5 == 0) 0 else 1)
            val result = buildString {
                append("(${page + 1}/${pagesCount}) Найдено <b>${benches.size}</b> лавочек в радиусе " +
                        "<b>${session.radius}</b> метров\n\n")
                val benchesSubList = benches.subList(page * 5, min(page * 5 + 5, benches.size))
                for ((index, value) in benchesSubList.withIndex()) {
                    val realIndex = index + 1 + page * 5
                    append("<b>${realIndex}.</b> $value\nПоказать на карте: /bench_${value.node.id}\n\n")
                }
            }
            val responses = mutableListOf<BaseRequest<*, *>>()
            callbackId?.let {
                responses.add(AnswerCallbackQuery(it))
            }
            responses.add(SendMessage(session.chatId, result)
                .parseMode(ParseMode.HTML)
                .replyMarkup(
                    InlineKeyboardMarkup(
                        InlineKeyboardButton("Назад").callbackData("back"),
                        InlineKeyboardButton("Дальше").callbackData("forward")
                    )
                ))
            return responses
        }
    }

}