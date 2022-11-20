package com.thorinhood.botFarm.engine.messages

interface MsgSender<MSG> {
    fun sendMessages(messages: List<MSG>)
    fun sendMessage(message: MSG)
}