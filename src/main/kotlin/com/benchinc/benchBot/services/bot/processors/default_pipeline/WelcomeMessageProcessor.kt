package com.benchinc.benchBot.services.bot.processors.default_pipeline

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.benchinc.benchBot.services.bot.processors.default_pipeline.transitions.ChangeRadiusStartMessageProcessor
import com.benchinc.benchBot.services.bot.processors.default_pipeline.transitions.StartAddingBench
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class WelcomeMessageProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "default"
        session.currentPipelineInfo.step = "?"
        return listOf(
            SendMessage(session.chatId, "Привет")
                .parseMode(ParseMode.HTML)
                .replyMarkup(DEFAULT_KEYBOARD)
        )
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.equals(NAME) ?: false

    companion object {
        const val NAME = "/start"
        val DEFAULT_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
                    arrayOf(
                        KeyboardButton(FindBenchesProcessor.NAME)
                            .requestLocation(true)),
                    arrayOf(KeyboardButton(ChangeRadiusStartMessageProcessor.NAME)),
                    arrayOf(KeyboardButton(StartAddingBench.NAME))
                ).resizeKeyboard(true)
    }
}