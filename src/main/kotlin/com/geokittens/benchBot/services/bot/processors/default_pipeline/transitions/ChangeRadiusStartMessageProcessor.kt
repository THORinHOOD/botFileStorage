package com.geokittens.benchBot.services.bot.processors.default_pipeline.transitions

import com.geokittens.benchBot.data.Session
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
@Pipeline("default")
class ChangeRadiusStartMessageProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        session.currentPipelineInfo.pipelineName = "change_radius"
        session.currentPipelineInfo.step = "choose_radius"
        return listOf(
            SendMessage(session.chatId, "Выберите радиус поиска")
                .replyMarkup(KEYBOARD)
        )
    }

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.equals(NAME) ?: false

    companion object {
        const val NAME = "\uD83C\uDF10 Изменить радиус поиска"
        val KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton("\uD83C\uDF10 50 метров"), KeyboardButton("\uD83C\uDF10 100 метров")),
            arrayOf(KeyboardButton("\uD83C\uDF10 200 метров"), KeyboardButton("\uD83C\uDF10 500 метров")),
            arrayOf(KeyboardButton("\uD83C\uDF10 1000 метров"), KeyboardButton("\uD83C\uDF10 1500 метров")),
            arrayOf(KeyboardButton(CancelPipelineProcessor.NAME))
        ).resizeKeyboard(true)
    }
}