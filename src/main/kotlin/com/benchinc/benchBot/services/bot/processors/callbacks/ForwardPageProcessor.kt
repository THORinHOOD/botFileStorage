package com.benchinc.benchBot.services.bot.processors.callbacks

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.helpers.BenchPageStrategy
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class ForwardPageProcessor(private val benchPageStrategy: BenchPageStrategy) : CallbackProcessor {

    override fun getCallbackName(): String = "forward"

    override fun process(session: Session, parameter: CallbackQuery): List<BaseRequest<*, *>> {
        return parameter.message()?.let { messagePage ->
            val page = benchPageStrategy.extractMessagePageNumber(messagePage.text())
            if ((page + 1) * 5 < session.currentBenches.size) {
                benchPageStrategy.buildPageWithBenches(session, page + 1, parameter.id())
            } else {
                listOf(AnswerCallbackQuery(parameter.id()))
            }
        } ?: listOf()
    }
}