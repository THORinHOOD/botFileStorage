package com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination

import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexConst
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexEntity
import com.thorinhood.botFarm.engine.pagination.*

import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.engine.sessions.Session
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