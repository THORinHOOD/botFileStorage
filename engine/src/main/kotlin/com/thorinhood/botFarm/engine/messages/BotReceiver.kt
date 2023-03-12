package com.thorinhood.botFarm.engine.messages

interface BotReceiver<OR, IR> {
    val converter: Converter<OR, IR>
    fun subscribe(process: (IR) -> Unit)
    fun start()
}