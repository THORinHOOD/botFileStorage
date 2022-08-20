package com.thorinhood.botFarm.engine.processors

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult

import com.thorinhood.botFarm.engine.sessions.Session

abstract class BaseProcessor(
    val name: String,
    val procSpace: String = ""
) {

    fun process(session: Session<Long>, update: Update) : List<BaseRequest<*, *>> {
        val result = processInner(session, update)
        val messages = result.messages?.toMutableList() ?: mutableListOf()
        if (result.transition != null) {
            result.transition.preTransitionAction?.let {
                it(session)
            }
            session.cursor.procSpace = result.transition.procSpace
            result.transition.makeMessage(session.sessionId)?.let {
                messages.add(0, it)
            }
            if (result.args != null) {
                session.cursor.args.putAll(result.args)
            }
        }
        return messages
    }

    fun isThisProcessor(session: Session<Long>, update: Update) : Boolean {
        if (session.cursor.procSpace != procSpace && procSpace != "") {
            return false
        }
        return isThisProcessorInner(session, update)
    }

    fun keyByIndex(depth: Int) : String {
        val left = indexOf(procSpace, "#", depth - 1) + 1
        val right = indexOf(procSpace, "#", depth, procSpace.length)
        return procSpace.substring(left, right)
    }

    protected fun isNotCancel(update: Update) : Boolean =
        update.message()?.text()?.let { it != BaseCancelProcessor.LABEL } ?: false

    protected fun isUpdateMessageEqualsLabel(update: Update, label: String) =
        update.message()?.text()?.let { it == label } ?: false

    protected fun isUpdateMessageContainsLabel(update: Update, label: String) =
        update.message()?.text()?.contains(label) ?: false

    private fun indexOf(str: String, sub: String, number: Int, default: Int = -1) : Int {
        if (number < 1) {
            return default
        }
        var from = -1
        for (i in 1..number) {
            from = str.indexOf(sub, from + 1)
            if (from == -1) return str.length
        }
        return from
    }

    protected abstract fun processInner(session: Session<Long>, update: Update) : ProcessResult
    protected abstract fun isThisProcessorInner(session: Session<Long>, update: Update) : Boolean

}