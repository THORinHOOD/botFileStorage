package com.thorinhood.fileStorageBot.bot.yandex_disk.utils

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.PaginationContext
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

class YandexUtils {

    companion object {
        fun getContext(session: Session) : PaginationContext<YandexEntity> =
            session.args.getOrPut(YandexConst.PAGINATION_CONTEXT) {
                PaginationContext<YandexEntity>(mutableMapOf("currentPath" to "disk:/"))
            } as PaginationContext<YandexEntity>

        fun getCurrentPath(session: Session) : String = getContext(session).context["currentPath"] as String
        fun setCurrentPath(session: Session, currentPath: String) {
            getContext(session).context["currentPath"] = currentPath
        }
    }
}