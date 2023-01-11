package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.delete

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.processors.Processor

import com.thorinhood.botFarm.fileStorageBot.KeyboardService
import com.thorinhood.botFarm.fileStorageBot.ProcSpaces

@Processor
class CancelDeleteEntityProcessor : BaseProcessor(
  "cancelDeleteEntity",
  ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_DELETE
) {

    override fun processInner(session: Session, update: Update): ProcessResult {
        return ProcessResult(listOf(
            SendMessage(session.sessionId, "Окей, не будем этого делать"),
            SendMessage(session.sessionId, "Что вы собираетесь делать с ${
                when(session.args["entityType"]) {
                    "file" -> " файлом ${session.args["entityName"]}"
                    "dir" -> " папкой ${session.args["entityName"]}"
                    else -> return ProcessResult.EMPTY_RESULT
                }
            } ?").replyMarkup(
                when(session.args["entityType"]) {
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