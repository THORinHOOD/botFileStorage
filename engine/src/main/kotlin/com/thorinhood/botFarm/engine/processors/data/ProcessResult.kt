package com.thorinhood.botFarm.engine.processors.data

import com.thorinhood.botFarm.configs.TelegramMessage

class ProcessResult(
    val messages: List<TelegramMessage>?,
    val transition: BaseTransition? = null,
    val postProcessAction: ((Session) -> Unit)? = null
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