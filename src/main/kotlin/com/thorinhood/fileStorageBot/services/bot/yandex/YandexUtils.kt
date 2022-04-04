package com.thorinhood.fileStorageBot.services.bot.yandex

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.api.YandexEntity

class YandexUtils {

    companion object {
        fun getContext(session: Session) : PaginationContext<YandexEntity> =
            session.args.getOrPut("yandex_pagination_context") {
                PaginationContext<YandexEntity>(mutableMapOf("currentPath" to "disk:/"))
            } as PaginationContext<YandexEntity>
    }
}