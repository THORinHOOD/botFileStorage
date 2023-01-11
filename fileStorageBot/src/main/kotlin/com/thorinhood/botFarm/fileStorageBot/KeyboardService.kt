package com.thorinhood.botFarm.fileStorageBot

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.botFarm.fileStorageBot.default_processors.transitions.StartYandexAuthProcessor
import com.thorinhood.botFarm.fileStorageBot.default_processors.transitions.StartYandexFileTreeProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.MoveToMainProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.PrevFolderProcessorBase
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.ShowBaseEntitiesProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.GetFileProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.NextFolderProcessorBase
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.delete.CancelDeleteEntityProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.delete.DeleteEntityProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.entity_manipulation.transitions.StartDeleteEntityProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.processors.file_tree.transitions.StartCreateFolderProcessor
import com.thorinhood.botFarm.fileStorageBot.yandex_disk.utils.YandexConst

import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.sessions.Session
import org.springframework.stereotype.Service

@Service
class KeyboardService {

    fun getDefaultKeyboard(session: Session) : ReplyKeyboardMarkup =
        session.args[YandexConst.TOKEN]?.let { DEFAULT_KEYBOARD } ?: UNAUTH_KEYBOARD

    companion object {
        private val UNAUTH_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartYandexAuthProcessor.LABEL))
        ).resizeKeyboard(true)
        private val DEFAULT_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartYandexFileTreeProcessor.LABEL))
        ).resizeKeyboard(true)

        val FILE_TREE_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(PrevFolderProcessorBase.LABEL)),
            arrayOf(KeyboardButton(ShowBaseEntitiesProcessor.LABEL)),
            arrayOf(KeyboardButton(StartCreateFolderProcessor.LABEL)),
            arrayOf(KeyboardButton(MoveToMainProcessor.LABEL))
        )
        val FILE_TREE_CREATE_FOLDER_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(BaseCancelProcessor.LABEL))
        )
        val FILE_TREE_FILE_MANIPULATION: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(GetFileProcessor.LABEL)),
            arrayOf(KeyboardButton(StartDeleteEntityProcessor.LABEL)),
            arrayOf(KeyboardButton(BaseCancelProcessor.LABEL))
        )
        val FILE_TREE_FOLDER_MANIPULATION: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(NextFolderProcessorBase.LABEL)),
            arrayOf(KeyboardButton(StartDeleteEntityProcessor.LABEL)),
            arrayOf(KeyboardButton(BaseCancelProcessor.LABEL))
        )
        val FILE_TREE_DELETE_CHOICE: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(DeleteEntityProcessor.IN_STASH)),
            arrayOf(KeyboardButton(DeleteEntityProcessor.PERMANENTLY)),
            arrayOf(KeyboardButton(CancelDeleteEntityProcessor.LABEL))
        )
    }

}