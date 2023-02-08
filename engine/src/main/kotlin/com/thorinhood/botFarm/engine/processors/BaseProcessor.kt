package com.thorinhood.botFarm.engine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.data.entities.TransitionsHistory
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import com.thorinhood.botFarm.engine.processors.data.Session

abstract class BaseProcessor(
    val name: String,
    val procSpace: String = ""
) {

    fun process(
        session: Session,
        update: Update
    ): List<BaseRequest<*, *>> {
        val result = processInner(session, update)
        val messages = result.messages?.toMutableList() ?: mutableListOf()
        session.transitionsHistory.makeTransition(session.sessionId, result.transition)?.let { message ->
            messages.add(0, message)
        }
        result.transition?.postTransitionAction?.invoke(session)
        result.postProcessAction?.let {
            it(session)
        }
        return messages
    }

    fun isThisProcessor(session: Session, update: Update): Boolean {
        if (session.transitionsHistory.getCurrentProcSpace() != procSpace && procSpace != "") {
            return false
        }
        return isThisProcessorInner(session, update)
    }

    protected fun isNotCancel(update: Update): Boolean =
        update.message()?.text()?.let { it != BaseCancelProcessor.LABEL } ?: false

    protected fun isUpdateMessageEqualsLabel(update: Update, label: String) =
        update.message()?.text()?.let { it == label } ?: false

    protected fun isUpdateMessageContainsLabel(update: Update, label: String) =
        update.message()?.text()?.contains(label) ?: false

    protected abstract fun processInner(session: Session, update: Update): ProcessResult
    protected abstract fun isThisProcessorInner(session: Session, update: Update): Boolean

}