package com.thorinhood.fileStorageBot.services.bot.description

import com.thorinhood.fileStorageBot.data.Entity

interface EntityDescriptionStrategy {
    fun description(index: Int, entityInfo: Entity): String
}