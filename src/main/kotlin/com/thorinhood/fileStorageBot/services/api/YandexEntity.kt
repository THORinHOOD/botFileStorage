package com.thorinhood.fileStorageBot.services.api

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.Entity

data class YandexEntity(
    val name: String = "",
    val type: String = "empty",
    val href: String?
) : Entity
