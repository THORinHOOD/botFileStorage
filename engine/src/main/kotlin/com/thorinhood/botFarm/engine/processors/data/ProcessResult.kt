package com.thorinhood.botFarm.engine.processors.data

import com.thorinhood.botFarm.configs.TelegramMessage
import com.thorinhood.botFarm.engine.sessions.Session

class ProcessResult(
    val messages: List<TelegramMessage>?,
    val transition: Transition? = null,
    val postProcessAction: ((Session<Long>) -> Unit)? = null
) {

    fun merge(processResult: ProcessResult) : ProcessResult {
        val messages = processResult.messages?.toMutableList()
        messages?.addAll(this.messages ?: listOf())
        return ProcessResult(
            messages,
            transition
        )
    }

    companion object {
        val EMPTY_RESULT = ProcessResult(listOf())
    }
}