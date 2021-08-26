package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.helpers.strategies.BenchPageStrategy
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AnswerCallbackQuery
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class ForwardPageProcessor(private val benchPageStrategy: BenchPageStrategy) : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.message()?.let { messagePage ->
                val page = benchPageStrategy.extractMessagePageNumber(messagePage.text())
                if ((page + 1) * 5 < session.currentBenches.size) {
                    benchPageStrategy.buildPageWithBenches(session, page + 1, callbackQuery.id())
                } else {
                    listOf(AnswerCallbackQuery(callbackQuery.id()))
                }
            }
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.callbackQuery()?.let { callbackQuery -> callbackQuery.data()?.equals(NAME) } ?: false

    companion object {
        const val NAME = "forward"
    }
}