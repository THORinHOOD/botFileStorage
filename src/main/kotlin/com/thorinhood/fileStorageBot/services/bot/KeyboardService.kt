package com.thorinhood.fileStorageBot.services.bot

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions.StartEntitiesProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions.StartAuthProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.MoveToMainProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.PrevFolderProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.ShowEntitiesProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.create_folder.StartCreateFolderProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.CancelEntityManipulationProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.GetFileProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.NextFolderProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.delete.DeleteEntityProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.entity_manipulation.delete.StartDeleteEntityProcessor
import org.springframework.stereotype.Service

@Service
class KeyboardService {

    fun getDefaultKeyboard(session: Session) : ReplyKeyboardMarkup =
        session.token?.let { token ->
            DEFAULT_KEYBOARD
        } ?: UNAUTH_KEYBOARD

    companion object {
        private val UNAUTH_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartAuthProcessor.NAME))
        ).resizeKeyboard(true)
        private val DEFAULT_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartEntitiesProcessor.NAME))
        ).resizeKeyboard(true)

        val FILE_TREE_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(PrevFolderProcessor.NAME)),
            arrayOf(KeyboardButton(ShowEntitiesProcessor.NAME)),
            arrayOf(KeyboardButton(StartCreateFolderProcessor.NAME)),
            arrayOf(KeyboardButton(MoveToMainProcessor.NAME))
        )
        val FILE_TREE_CREATE_FOLDER_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(BaseCancelProcessor.NAME))
        )
        val FILE_TREE_FILE_MANIPULATION: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(GetFileProcessor.NAME)),
            arrayOf(KeyboardButton(StartDeleteEntityProcessor.NAME)),
            arrayOf(KeyboardButton(BaseCancelProcessor.NAME))
        )
        val FILE_TREE_FOLDER_MANIPULATION: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(NextFolderProcessor.NAME)),
            arrayOf(KeyboardButton(StartDeleteEntityProcessor.NAME)),
            arrayOf(KeyboardButton(BaseCancelProcessor.NAME))
        )
        val FILE_TREE_DELETE_CHOICE: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(DeleteEntityProcessor.IN_STASH)),
            arrayOf(KeyboardButton(DeleteEntityProcessor.PERMANENTLY)),
            arrayOf(KeyboardButton(BaseCancelProcessor.NAME))
        )
    }

}