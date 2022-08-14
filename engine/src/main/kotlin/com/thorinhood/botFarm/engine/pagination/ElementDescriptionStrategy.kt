package com.thorinhood.botFarm.engine.pagination

interface ElementDescriptionStrategy<T> {
    fun description(index: Int, entity: T) : String
}