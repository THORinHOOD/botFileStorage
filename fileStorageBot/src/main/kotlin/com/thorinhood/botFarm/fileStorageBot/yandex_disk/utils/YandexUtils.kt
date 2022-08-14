package com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils

import com.thorinhood.botFarm.engine.pagination.PaginationContext
import com.thorinhood.botFarm.engine.sessions.Session


class YandexUtils {

    companion object {
        fun getContext(session: Session<Long>) : PaginationContext<YandexEntity> =
            session.args.getOrPut(YandexConst.PAGINATION_CONTEXT) {
                PaginationContext<YandexEntity>(mutableMapOf("currentPath" to "disk:/"))
            } as PaginationContext<YandexEntity>

        fun getCurrentPath(session: Session<Long>) : String = getContext(session).context["currentPath"] as String
        fun setCurrentPath(session: Session<Long>, currentPath: String) {
            getContext(session).context["currentPath"] = currentPath
        }
    }
}