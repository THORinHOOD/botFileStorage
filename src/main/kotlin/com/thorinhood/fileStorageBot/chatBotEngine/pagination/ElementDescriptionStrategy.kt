package com.thorinhood.fileStorageBot.chatBotEngine.pagination

interface ElementDescriptionStrategy<T : Entity> {
    fun description(index: Int, entity: T) : String
}