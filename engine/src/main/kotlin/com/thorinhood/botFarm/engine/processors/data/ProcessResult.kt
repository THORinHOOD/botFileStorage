package com.thorinhood.botFarm.engine.processors.data

import com.pengrad.telegrambot.request.BaseRequest

class ProcessResult(
    val messages: List<BaseRequest<*, *>>?,
    val transition: Transition? = null,
    val args: MutableMap<String, Any>? = null
) {

    fun merge(processResult: ProcessResult) : ProcessResult {
        val messages = processResult.messages?.toMutableList()
        messages?.addAll(this.messages ?: listOf())
        args?.putAll(processResult.args ?: mapOf())
        return ProcessResult(
            messages,
            transition,
            args
        )
    }

    companion object {
        val EMPTY_RESULT = ProcessResult(listOf())
    }
}