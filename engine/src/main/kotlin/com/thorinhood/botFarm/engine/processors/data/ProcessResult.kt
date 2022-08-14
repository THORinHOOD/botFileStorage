package com.thorinhood.botFarm.engine.processors.data

import com.pengrad.telegrambot.request.BaseRequest

class ProcessResult(
    val messages: List<BaseRequest<*, *>>?,
    val transition: com.thorinhood.botFarm.engine.processors.data.Transition? = null,
    val args: MutableMap<String, Any>? = null
) {

    fun merge(processResult: com.thorinhood.botFarm.engine.processors.data.ProcessResult) : com.thorinhood.botFarm.engine.processors.data.ProcessResult {
        val messages = processResult.messages?.toMutableList()
        messages?.addAll(this.messages ?: listOf())
        args?.putAll(processResult.args ?: mapOf())
        return com.thorinhood.botFarm.engine.processors.data.ProcessResult(
            messages,
            transition,
            args
        )
    }

    companion object {
        val EMPTY_RESULT = com.thorinhood.botFarm.engine.processors.data.ProcessResult(listOf())
    }
}