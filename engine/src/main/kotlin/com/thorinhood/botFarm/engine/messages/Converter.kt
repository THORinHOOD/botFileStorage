package com.thorinhood.botFarm.engine.messages

interface Converter<FROM, TO> {
    fun convert(message: FROM) : TO
}