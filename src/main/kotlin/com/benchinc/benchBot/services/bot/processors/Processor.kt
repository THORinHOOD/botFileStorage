package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.BaseRequest

interface Processor<PARAMETER> {
    fun process(session: Session, parameter: PARAMETER) : List<BaseRequest<*, *>>
}