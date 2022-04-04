package com.thorinhood.fileStorageBot.chatBotEngine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.fileStorageBot.chatBotEngine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

abstract class BaseProcessor(
    val name: String,
    private val procSpace: String = ""
) {

    fun process(session: Session, update: Update) : List<BaseRequest<*, *>> {
        val result = processInner(session, update)
        val messages = result.messages?.toMutableList() ?: mutableListOf()
        if (result.transition != null) {
            session.cursor.procSpace = result.transition.procSpace
            result.transition.makeMessage(session.chatId)?.let {
                messages.add(0, it)
            }
            if (result.args != null) {
                session.cursor.args.putAll(result.args)
            }
        }
        return messages
    }

    fun isThisProcessor(session: Session, update: Update) : Boolean {
        if (session.cursor.procSpace != procSpace && procSpace != "") {
            return false
        }
        return isThisProcessorInner(session, update)
    }

    protected fun isNotCancel(update: Update) : Boolean =
        update.message()?.text()?.let { it != BaseCancelProcessor.LABEL } ?: false

    protected fun isUpdateMessageEqualsLabel(update: Update, label: String) =
        update.message()?.text()?.let { it == label } ?: false

    protected fun isUpdateMessageContainsLabel(update: Update, label: String) =
        update.message()?.text()?.contains(label) ?: false

    protected abstract fun processInner(session: Session, update: Update) : ProcessResult
    protected abstract fun isThisProcessorInner(session: Session, update: Update) : Boolean

}