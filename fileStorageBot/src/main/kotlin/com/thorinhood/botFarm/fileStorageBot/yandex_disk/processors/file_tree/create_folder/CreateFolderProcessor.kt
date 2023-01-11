package com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.create_folder

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
import com.thorinhood.botFarm.engine.processors.data.Transition

@Processor
class CreateFolderProcessor(
    private val yandexDisk: YandexDisk,
    yandexEntityPageStrategy: YandexEntityPageStrategy
) : YandexBaseEntitiesProcessor(
    yandexDisk,
    yandexEntityPageStrategy,
    "createFolder",
    ProcSpaces.YANDEX_FILE_TREE_ENTITY_MANIPULATION_CREATE_FOLDER
) {

    override fun processInner(
        session: Session,
        update: Update
    ): ProcessResult =
        getEntities(session).merge(ProcessResult(update.message()?.text()?.let { folderName ->
            val folderPath = "${YandexUtils.getCurrentPath(session)}$folderName/"
            val created = yandexDisk.createFolder(session.args[YandexConst.TOKEN] as String, folderPath)
            listOf(SendMessage(session.sessionId, if (created) {
                "Создана папка [$folderPath]"
            } else {
                "Не удалось создать папку [$folderName]"
            }))
        } ?: listOf(),
        Transition(ProcSpaces.YANDEX_FILE_TREE)))

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)

}