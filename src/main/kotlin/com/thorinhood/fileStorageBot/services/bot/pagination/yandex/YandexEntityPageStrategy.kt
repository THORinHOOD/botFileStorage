package com.thorinhood.fileStorageBot.services.bot.pagination.yandex

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.*
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.services.api.YandexDisk
import com.thorinhood.fileStorageBot.services.api.YandexEntity
import com.thorinhood.fileStorageBot.services.bot.yandex.YandexUtils
import org.springframework.stereotype.Service

@Service
class YandexEntityPageStrategy(
    elementDescriptionStrategy: ElementDescriptionStrategy<YandexEntity>,
    private val yandexDisk: YandexDisk
) : BasePageStrategy<YandexEntity>(elementDescriptionStrategy) {

    override fun paginationContextExtract(session: Session): PaginationContext<YandexEntity> =
        YandexUtils.getContext(session)

    override fun getElements(session: Session, pageCallback: PageCallback,
                             paginationType: PaginationType): ElementsResponse<YandexEntity> =
        yandexDisk.getEntities(
            session.args["yandex_token"] as String,
            (session.args["yandex_pagination_context"] as PaginationContext<YandexEntity>)
                .context["currentPath"] as String,
            when (paginationType) {
                PaginationType.FORWARD -> pageCallback.page + 1
                PaginationType.BACK -> pageCallback.page - 1
            } * pageCallback.pageSize, pageCallback.pageSize)


}