package com.thorinhood.fileStorageBot.bot.yandex_disk.utils.pagination

import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexConst
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexEntity
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.*
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.api.YandexDisk
import org.springframework.stereotype.Service

@Service
class YandexEntityPageStrategy(
    yandexEntityDescriptionStrategy: YandexEntityDescriptionStrategy,
    private val yandexDisk: YandexDisk
) : BasePageStrategy<YandexEntity>(yandexEntityDescriptionStrategy) {

    override fun paginationContextExtract(session: Session): PaginationContext<YandexEntity> =
        YandexUtils.getContext(session)

    override fun getElements(session: Session, pageCallback: PageCallback,
                             paginationType: PaginationType): ElementsResponse<YandexEntity> =
        yandexDisk.getEntities(
            session.args[YandexConst.TOKEN] as String,
            YandexUtils.getCurrentPath(session),
            when (paginationType) {
                PaginationType.FORWARD -> pageCallback.page + 1
                PaginationType.BACK -> pageCallback.page - 1
            } * pageCallback.pageSize, pageCallback.pageSize)

}