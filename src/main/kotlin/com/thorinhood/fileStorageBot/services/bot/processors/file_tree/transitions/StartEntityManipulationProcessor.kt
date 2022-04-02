package com.thorinhood.fileStorageBot.services.bot.processors.file_tree.transitions

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.services.bot.KeyboardService

@Processor
class StartEntityManipulationProcessor : BaseProcessor(
    "startEntityManipulation",
    "file_tree"
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult {
        val entityName = update.message()?.text()?.substring(1) ?: return ProcessResult.EMPTY_RESULT
        val entityType = update.message()?.text()?.let {
            session.fileTreeInfo.indexToEntity[it]?.type
        } ?: return ProcessResult.EMPTY_RESULT
        val arg = mutableMapOf<String, Any>("entity" to (update.message()?.text() ?: return ProcessResult.EMPTY_RESULT),
            "entity_type" to entityType,
            "entity_name" to entityName)
        return ProcessResult(listOf(SendMessage(session.chatId, "Что вы собираетесь делать с ${
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
        Transition("file_tree#entity_manipulation"),
        arg)
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isEntity(session, update, setOf("file", "dir"))

    private fun isEntity(session: Session, update: Update, types: Set<String>) : Boolean =
        update.message()?.text()?.let { text ->
            text.startsWith("/") && session.fileTreeInfo.indexToEntity.contains(text) &&
                    types.contains(session.fileTreeInfo.indexToEntity[text]?.type)
        } ?: false

}