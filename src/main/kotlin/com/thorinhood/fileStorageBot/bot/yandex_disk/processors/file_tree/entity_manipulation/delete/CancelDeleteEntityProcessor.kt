package com.thorinhood.fileStorageBot.bot.yandex_disk.processors.file_tree.entity_manipulation.delete

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.fileStorageBot.chatBotEngine.processors.BaseProcessor
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.ProcessResult
import com.thorinhood.fileStorageBot.chatBotEngine.processors.data.Transition
import com.thorinhood.fileStorageBot.chatBotEngine.processors.Processor
import com.thorinhood.fileStorageBot.chatBotEngine.sessions.Session
import com.thorinhood.fileStorageBot.bot.KeyboardService
import com.thorinhood.fileStorageBot.bot.ProcSpaces

@Processor
class CancelDeleteEntityProcessor : BaseProcessor(
  "cancelDeleteEntity",
  ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_DELETE
) {

    override fun processInner(session: Session, update: Update): ProcessResult {
        return ProcessResult(listOf(
            SendMessage(session.chatId, "Окей, не будем этого делать"),
            SendMessage(session.chatId, "Что вы собираетесь делать с ${
                when(session.cursor.args["entityType"]) {
                    "file" -> " файлом ${session.cursor.args["entityName"]}"
                    "dir" -> " папкой ${session.cursor.args["entityName"]}"
                    else -> return ProcessResult.EMPTY_RESULT
                }
            } ?").replyMarkup(
                when(session.cursor.args["entityType"]) {
                    "file" -> KeyboardService.FILE_TREE_FILE_MANIPULATION
                    "dir" -> KeyboardService.FILE_TREE_FOLDER_MANIPULATION
                    else -> null
                }
            )),
            Transition(ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION))
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isUpdateMessageEqualsLabel(update, LABEL)

    companion object {
        const val LABEL = "Отмена"
    }
}