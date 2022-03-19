package com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.KeyboardService
import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.stereotype.Service

@Service
@Pipeline("file_tree")
class EntityManipulationProcessor : Processor {

    override val name: String = NAME

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> {
        val entityName = update.message()?.text()?.substring(1) ?: return listOf()
        val entityType = update.message()?.text()?.let {
            session.fileTreeInfo.indexToEntity[it]?.type
        } ?: return listOf()
        session.argument = update.message()?.text() ?: return listOf()
        return listOf(SendMessage(session.chatId, "Что вы собираетесь делать с ${
            when(entityType) {
                "file" -> " файлом $entityName"
                "dir" -> " папкой $entityName"
                else -> return listOf()
            }
        } ?").replyMarkup(
            when(entityType) {
                "file" -> KeyboardService.FILE_TREE_FILE_MANIPULATION
                "dir" -> KeyboardService.FILE_TREE_FOLDER_MANIPULATION
                else -> null
            }
        ))
    }

    override fun isThisProcessorMessage(session: Session, update: Update): Boolean =
        isEntity(session, update, setOf("file", "dir"))


    private fun isEntity(session: Session, update: Update, types: Set<String>) : Boolean =
        update.message()?.text()?.let { text ->
            text.startsWith("/") && session.fileTreeInfo.indexToEntity.contains(text) &&
                    types.contains(session.fileTreeInfo.indexToEntity[text]?.type)
        } ?: false

    companion object {
        const val NAME = "file_tree_entity_manipulation"
    }
}