package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage

import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces
import com.thorinhood.fileStorageBot.bot.yandex_disk.utils.YandexUtils
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session

@Processor
class StartEntityManipulationProcessor : BaseProcessor(
    "startEntityManipulation",
    ProcSpaces.YANDEX_FILE_TREE
) {

    override fun processInner(
        session: Session<Long>,
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
        Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION), arg)
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isEntity(session, update, setOf("file", "dir"))

    private fun isEntity(session: Session<Long>, update: Update, types: Set<String>) : Boolean =
        update.message()?.text()?.let { text ->
            text.startsWith("/") && YandexUtils.getContext(session).elementsMap.contains(text) &&
                    types.contains(YandexUtils.getContext(session).elementsMap[text]?.type)
        } ?: false

}