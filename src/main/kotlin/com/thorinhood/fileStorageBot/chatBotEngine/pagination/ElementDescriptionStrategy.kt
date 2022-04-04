package com.thorinhood.fileStorageBot.chatBotEngine.pagination

interface ElementDescriptionStrategy<T> {
    fun description(index: Int, entity: T) : String
}