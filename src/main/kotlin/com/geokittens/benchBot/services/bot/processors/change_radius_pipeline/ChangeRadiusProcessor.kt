package com.geokittens.benchBot.services.bot.processors.change_radius_pipeline

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import com.geokittens.benchBot.services.bot.processors.default_pipeline.WelcomeMessageProcessor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
@Pipeline("change_radius")
class ChangeRadiusProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update) : List<BaseRequest<*, *>> =
        update.message()?.text()?.let { message ->
            session.radius = message.substring(3, message.indexOf(" метров")).toInt()
            session.currentPipelineInfo.pipelineName = "default"
            session.currentPipelineInfo.step = "?"
            listOf(SendMessage(session.chatId, "Радиус поиска изменён на ${session.radius} метров вокруг")
                .replyMarkup(WelcomeMessageProcessor.DEFAULT_KEYBOARD))
        } ?: listOf()

    override fun isThisProcessorMessage(update: Update): Boolean =
        update.message()?.text()?.contains(" метров") ?: false &&
        update.message()?.text()?.contains("\uD83C\uDF10") ?: false

    companion object {
        const val NAME = "choose_radius"
    }
}