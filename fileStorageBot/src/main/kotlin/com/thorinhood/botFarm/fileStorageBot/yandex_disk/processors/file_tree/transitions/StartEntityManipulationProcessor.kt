package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexUtils
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class StartEntityManipulationProcessor : BaseProcessor(
    "startEntityManipulation",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val entityName = YandexUtils.getContext(session).elementsMap[update.message()?.text()]?.name
            ?: return ProcessResult.EMPTY_RESULT
        val entityType = update.message()?.text()?.let {
            YandexUtils.getContext(session).elementsMap[it]?.type
        } ?: return ProcessResult.EMPTY_RESULT
        val arg = mutableMapOf<String, Any>("entity" to (update.message()?.text() ?: return ProcessResult.EMPTY_RESULT),
            "entity_type" to entityType,
            "entity_name" to entityName,
            "entity_index" to update.message()?.text()!!)
        return ProcessResult(listOf(SendMessage(session.sessionId, "Что вы собираетесь делать с ${
            when(entityType) {
                "file" -> " файлом $entityName"
                "dir" -> " папкой $entityName"
                else -> return ProcessResult.EMPTY_RESULT
            }
        } ?").replyMarkup(
            when(entityType) {
                "file" -> KeyboardService.FILE_TREE_FILE_MANIPULATION
                "dir" -> KeyboardService.FILE_TREE_FOLDER_MANIPULATION
                else -> null
            }
        )),
        Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isEntity(session, update, setOf("file", "dir"))

    private fun isEntity(session: Session, update: Update, types: Set<String>) : Boolean =
        update.message()?.text()?.let { text ->
            text.startsWith("/") && YandexUtils.getContext(session).elementsMap.contains(text) &&
                    types.contains(YandexUtils.getContext(session).elementsMap[text]?.type)
        } ?: false

}