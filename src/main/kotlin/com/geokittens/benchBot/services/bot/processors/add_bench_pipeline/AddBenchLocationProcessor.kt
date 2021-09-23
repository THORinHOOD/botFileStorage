package com.geokittens.benchBot.services.bot.processors.add_bench_pipeline

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.RequestsServiceLocal
import com.geokittens.benchBot.services.bot.processors.CancelPipelineProcessor
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("add_bench")
class AddBenchLocationProcessor(private val requestsServiceLocal: RequestsServiceLocal) : Processor {
    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        return update.message()?.location()?.let { location ->
            requestsServiceLocal.addRequest(session.chatId, location)
            session.currentPipelineInfo.pipelineName = "add_bench"
            session.currentPipelineInfo.step = AddBenchPhotoProcessor.NAME
            listOf(SendMessage(session.chatId, "Теперь пришли её фотографию, чтобы мы могли проверить " +
                    "достоверность её существования").replyMarkup(KEYBOARD))
        } ?: listOf()
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.location() != null

    companion object {
        const val NAME = "\uD83D\uDCCD Локация лавочки"
        val KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(CancelPipelineProcessor.NAME))
        ).resizeKeyboard(true)
    }
}