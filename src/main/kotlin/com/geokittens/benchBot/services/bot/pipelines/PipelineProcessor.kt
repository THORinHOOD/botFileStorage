package com.geokittens.benchBot.services.bot.pipelines

import com.geokittens.benchBot.data.Session
import com.geokittens.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.BaseRequest

class PipelineProcessor(val name : String,
                        private val mapProcessors: Map<String, Processor>,
) {

    fun <T : AbstractSendRequest<T>> T.addReply(messageId: Int?) : T {
        messageId?.let {
            this.replyToMessageId(it)
        }
        return this
    }

    fun process(session: Session, update: Update) : List<BaseRequest<*, *>> =
        if (update.message()?.text()?.equals("Отмена") == true && mapProcessors.containsKey("Отмена")) {
            mapProcessors["Отмена"]!!.process(session, update)
        } else if (session.currentPipelineInfo.step == "?") {
            val foundProcessors = mapProcessors.filter { (_, processor) -> processor.isThisProcessorMessage(update) }
            if (foundProcessors.size > 1) {
                throw IllegalArgumentException("More than 1 processors of pipeline $name can take message")
            } else if (foundProcessors.size == 1) {
                foundProcessors.values.first().process(session, update)
            } else {
                listOf()
            }
        } else {
            mapProcessors[session.currentPipelineInfo.step]?.process(session, update)
                ?: throw IllegalArgumentException("Can't find step ${session.currentPipelineInfo.step} in pipeline " +
                        name)
        }

}