package com.benchinc.benchBot.services.bot.processors.default_pipeline.transitions

import com.benchinc.benchBot.data.Session
import com.benchinc.benchBot.services.bot.processors.CancelPipelineProcessor
import com.benchinc.benchBot.services.bot.processors.Pipeline
import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("default")
class StartAddingBench : Processor {
    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "add_bench"
        session.currentPipelineInfo.step = "add_bench_location"
        return listOf(
            SendMessage(session.chatId, "Чтобы добавить лавочку в OpenStreetMap пришли её геолокацию")
                .parseMode(ParseMode.HTML)
                .replyMarkup(
                    ReplyKeyboardMarkup(
                        arrayOf(
                            KeyboardButton("\uD83D\uDCCD Локация лавочки")
                                .requestLocation(true)),
                        arrayOf(KeyboardButton(CancelPipelineProcessor.NAME))
                    ).resizeKeyboard(true)))
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.equals(NAME) ?: false

    companion object {
        const val NAME = "Добавить лавочку"
    }

}