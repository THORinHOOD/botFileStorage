package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.delete

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexConst
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.api.YandexDisk
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.baseProcessors.YandexBaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.pagination.YandexEntityPageStrategy

import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class DeleteEntityProcessor(
    private val yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "deleteEntity",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_DELETE
) {

    override fun processInner(
        session: Session<Long>,
        update: Update
    ): ProcessResult {
        val inStash = update.message()?.text()?.let { text ->
            when (text) {
                PERMANENTLY -> false
                IN_STASH -> true
                else -> return ProcessResult.EMPTY_RESULT
            }
        } ?: return ProcessResult.EMPTY_RESULT
        val entityName = YandexUtils.getContext(session).elementsMap[session.cursor.args["entity"]]?.name ?:
            return ProcessResult(listOf(SendMessage(session.sessionId, "Не удалось удалить")))
        val result = yandexDisk.deleteEntity(session.args[YandexConst.TOKEN] as String,
            YandexUtils.getCurrentPath(session) + entityName,
            !inStash)
        return getEntities(session).merge(
            ProcessResult(listOf(if (result) {
                 SendMessage(session.sessionId, "[$entityName] ${
                     if (inStash) {
                         "Перемещено в корзину"
                     } else {
                         "Удалено перманентно"
                     }}")
             } else {
                 SendMessage(session.sessionId, "Не удалось удалить $entityName")
             }))
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, PERMANENTLY) ||
        isUpdateMessageEqualsLabel(update, IN_STASH)

    companion object {
        const val PERMANENTLY = "Перманентно"
        const val IN_STASH = "Поместить в корзину"
    }
}